package akkount.service;

import akkount.entity.Balance;
import akkount.entity.Operation;
import akkount.event.BalanceChangedEvent;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.FluentValueLoader;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Component(BalanceWorker.NAME)
public class BalanceWorker {

    public static final String NAME = "akk_BalanceWorker";

    @Inject
    private Metadata metadata;

    @Inject
    private Events events;

    @Inject
    private DataManager dataManager;

    @Inject
    private TransactionalDataManager txDataManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onBalanceChanged(EntityChangedEvent<Balance, UUID> event) {
        events.publish(new BalanceChangedEvent(this));
    }

    public BigDecimal getBalance(final UUID accountId, final Date date) {
        Optional<Balance> optStartBalance = dataManager.load(Balance.class)
                .query("select b from akk$Balance b where b.account.id = :accId and b.balanceDate < :balDate order by b.balanceDate desc")
                .parameter("accId", accountId)
                .parameter("balDate", date)
                .maxResults(1)
                .optional();

        BigDecimal startAmount = optStartBalance.map(Balance::getAmount).orElse(BigDecimal.ZERO);

        String expenseQueryStr = "select sum(o.amount1) from akk$Operation o where o.acc1.id = :accId and o.opDate <= :date1";
        if (optStartBalance.isPresent())
            expenseQueryStr += " and o.opDate >= :date2";

        FluentValueLoader<BigDecimal> expLoader = dataManager.loadValue(expenseQueryStr, BigDecimal.class)
                .parameter("accId", accountId)
                .parameter("date1", date);
        if (optStartBalance.isPresent())
            expLoader.parameter("date2", optStartBalance.get().getBalanceDate());
        BigDecimal expense = expLoader.one();
        if (expense == null)
            expense = BigDecimal.ZERO;

        String incomeQueryStr = "select sum(o.amount2) from akk$Operation o where o.acc2.id = :accId and o.opDate <= :date1";
        if (optStartBalance.isPresent())
            incomeQueryStr += " and o.opDate >= :date2";
        FluentValueLoader<BigDecimal> incLoader = dataManager.loadValue(incomeQueryStr, BigDecimal.class)
                .parameter("accId", accountId)
                .parameter("date1", date);
        if (optStartBalance.isPresent())
            incLoader.parameter("date2", optStartBalance.get().getBalanceDate());
        BigDecimal income = incLoader.one();
        if (income == null)
            income = BigDecimal.ZERO;

        return startAmount.add(income).subtract(expense);
    }

    @Transactional
    public void recalculateBalance(UUID accountId) {
        removeBalanceRecords(accountId);

        TreeMap<Date, Balance> balances = new TreeMap<>();

        List<Operation> operations = txDataManager.load(Operation.class)
                .query("select op from akk$Operation op " +
                        "left join op.acc1 a1 left join op.acc2 a2 " +
                        "where (a1.id = :accId or a2.id = :accId) order by op.opDate")
                .parameter("accId", accountId)
                .view("operation-recalc-balance")
                .list();

        for (Operation operation : operations) {
            addOperation(balances, operation, accountId);
        }
        for (Balance balance : balances.values()) {
            txDataManager.save(balance);
        }
    }

    private void removeBalanceRecords(UUID accountId) {
        txDataManager.load(Balance.class)
                .query("select b from akk$Balance b where b.account.id = :accId")
                .parameter("accId", accountId)
                .list()
                .forEach(balance -> txDataManager.remove(balance));
    }

    private void addOperation(TreeMap<Date, Balance> balances, Operation operation, UUID accountId) {
        if (operation.getAcc1() != null && operation.getAcc1().getId().equals(accountId)) {
            Map.Entry<Date, Balance> entry = balances.higherEntry(operation.getOpDate());
            if (entry == null) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc1());
                balance.setAmount(operation.getAmount1().negate()
                        .add(previousBalanceAmount(balances, operation.getOpDate())));
                balance.setBalanceDate(DateUtils.ceiling(operation.getOpDate(), Calendar.MONTH));
                balances.put(balance.getBalanceDate(), balance);
            } else {
                Balance balance = entry.getValue();
                balance.setAmount(balance.getAmount().subtract(operation.getAmount1()));
            }
        }
        if (operation.getAcc2() != null && operation.getAcc2().getId().equals(accountId)) {
            Map.Entry<Date, Balance> entry = balances.higherEntry(operation.getOpDate());
            if (entry == null) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc2());
                balance.setAmount(operation.getAmount2()
                        .add(previousBalanceAmount(balances, operation.getOpDate())));
                balance.setBalanceDate(DateUtils.ceiling(operation.getOpDate(), Calendar.MONTH));
                balances.put(balance.getBalanceDate(), balance);
            } else {
                Balance balance = entry.getValue();
                balance.setAmount(balance.getAmount().add(operation.getAmount2()));
            }
        }
    }

    private BigDecimal previousBalanceAmount(TreeMap<Date, Balance> balances, Date opDate) {
        Map.Entry<Date, Balance> entry = balances.floorEntry(opDate);
        return entry == null ? BigDecimal.ZERO : entry.getValue().getAmount();
    }
}
