package akkount.service;

import akkount.entity.Balance;
import akkount.entity.Operation;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.FluentValueLoader;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Component(BalanceWorker.NAME)
public class BalanceWorker {

    public static final String NAME = "akk_BalanceWorker";

    @Inject
    protected Persistence persistence;

    public BigDecimal getBalance(final UUID accountId, final Date date) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<Balance> balQuery = em.createQuery(
                    "select b from akk$Balance b where b.account.id = ?1 and b.balanceDate < ?2 order by b.balanceDate desc",
                    Balance.class);
            balQuery.setParameter(1, accountId);
            balQuery.setParameter(2, date);
            balQuery.setMaxResults(1);
            Balance startBalance = balQuery.getFirstResult();
            BigDecimal startAmount = startBalance != null ? startBalance.getAmount() : BigDecimal.ZERO;

            String expenseQueryStr = "select sum(o.amount1) from akk$Operation o where o.acc1.id = ?1 and o.opDate <= ?2";
            if (startBalance != null)
                expenseQueryStr += " and o.opDate >= ?3";
            Query expenseQuery = em.createQuery(expenseQueryStr);
            expenseQuery.setParameter(1, accountId);
            expenseQuery.setParameter(2, date);
            if (startBalance != null)
                expenseQuery.setParameter(3, startBalance.getBalanceDate());
            BigDecimal expense = (BigDecimal) expenseQuery.getSingleResult();
            if (expense == null)
                expense = BigDecimal.ZERO;

            String incomeQueryStr = "select sum(o.amount2) from akk$Operation o where o.acc2.id = ?1 and o.opDate <= ?2";
            if (startBalance != null)
                incomeQueryStr += " and o.opDate >= ?3";
            Query incomeQuery = em.createQuery(incomeQueryStr);
            incomeQuery.setParameter(1, accountId);
            incomeQuery.setParameter(2, date);
            if (startBalance != null)
                incomeQuery.setParameter(3, startBalance.getBalanceDate());
            BigDecimal income = (BigDecimal) incomeQuery.getSingleResult();
            if (income == null)
                income = BigDecimal.ZERO;

            return startAmount.add(income).subtract(expense);
        }
    }

    public void recalculateBalance(UUID accountId) {
        try (Transaction tx = persistence.createTransaction()) {
            removeBalanceRecords(accountId);

            TreeMap<Date, Balance> balances = new TreeMap<>();

            EntityManager em = persistence.getEntityManager();
            TypedQuery<Operation> query = em.createQuery("select op from akk$Operation op " +
                    "left join op.acc1 a1 left join op.acc2 a2 " +
                    "where (a1.id = ?1 or a2.id = ?1) order by op.opDate", Operation.class);
            query.setParameter(1, accountId);
            query.setViewName("operation-recalc-balance");
            List<Operation> operations = query.getResultList();
            for (Operation operation : operations) {
                addOperation(balances, operation, accountId);
            }
            for (Balance balance : balances.values()) {
                em.persist(balance);
            }

            tx.commit();
        }
    }

    private void removeBalanceRecords(UUID accountId) {
        EntityManager em = persistence.getEntityManager();
        TypedQuery<Balance> query = em.createQuery("select b from akk$Balance b where b.account.id = ?1", Balance.class);
        query.setParameter(1, accountId);
        List<Balance> list = query.getResultList();
        for (Balance balance : list) {
            em.remove(balance);
        }
    }

    private void addOperation(TreeMap<Date, Balance> balances, Operation operation, UUID accountId) {
        if (operation.getAcc1() != null && operation.getAcc1().getId().equals(accountId)) {
            Map.Entry<Date, Balance> entry = balances.higherEntry(operation.getOpDate());
            if (entry == null) {
                Balance balance = new Balance();
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
                Balance balance = new Balance();
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
