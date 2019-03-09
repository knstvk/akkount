package akkount.service;

import akkount.entity.Account;
import akkount.entity.Balance;
import akkount.entity.Operation;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Component(OperationWorker.NAME)
public class OperationWorker {

    public static final String NAME = "akk_OperationWorker";

    @Inject
    private Metadata metadata;

    @Inject
    private TransactionalDataManager txDataManager;

    @Inject
    private DataManager dataManager;

    @Inject
    private UserDataWorker userDataWorker;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    private void onOperationChanged(EntityChangedEvent<Operation, UUID> event) {
        Operation operation = txDataManager.load(event.getEntityId()).view("operation-with-accounts").one();
        switch (event.getType()) {
            case CREATED:
                addOperation(operation);
                saveUserData(operation);
                break;
            case UPDATED:
                removeOperation(getOldOperation(event.getEntityId()));
                addOperation(operation);
                saveUserData(operation);
                break;
            case DELETED:
                removeOperation(operation);
        }
    }

    private Operation getOldOperation(Id<Operation, UUID> operationId) {
        // Get old operation state in separate transaction
        return dataManager.load(operationId).view("operation-with-accounts").one();
    }

    private void removeOperation(Operation operation) {
        if (operation.getAcc1() != null) {
            List<Balance> list = getBalanceRecords(operation, operation.getAcc1());
            if (!list.isEmpty()) {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().add(operation.getAmount1()));
                    txDataManager.save(balance);
                }
            }
        }

        if (operation.getAcc2() != null) {
            List<Balance> list = getBalanceRecords(operation, operation.getAcc2());
            if (!list.isEmpty()) {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().subtract(operation.getAmount2()));
                    txDataManager.save(balance);
                }
            }
        }
    }

    private void addOperation(Operation operation) {
        if (operation.getAcc1() != null) {
            List<Balance> list = getBalanceRecords(operation, operation.getAcc1());
            if (list.isEmpty()) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc1());
                balance.setAmount(operation.getAmount1().negate()
                        .add(previousBalanceAmount(operation.getAcc1(), operation.getOpDate())));
                balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
                txDataManager.save(balance);
            } else {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().subtract(operation.getAmount1()));
                    txDataManager.save(balance);
                }
            }
        }

        if (operation.getAcc2() != null) {
            List<Balance> list = getBalanceRecords(operation, operation.getAcc2());
            if (list.isEmpty()) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc2());
                balance.setAmount(operation.getAmount2()
                        .add(previousBalanceAmount(operation.getAcc2(), operation.getOpDate())));
                balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
                txDataManager.save(balance);
            } else {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().add(operation.getAmount2()));
                    txDataManager.save(balance);
                }
            }
        }
    }

    private List<Balance> getBalanceRecords(Operation operation, Account acc) {
        return txDataManager.load(Balance.class)
                .query("select b from akk$Balance b " +
                        "where b.account.id = :accountId and b.balanceDate > :balanceDate order by b.balanceDate")
                .parameter("accountId", acc.getId())
                .parameter("balanceDate", operation.getOpDate())
                .list();
    }

    private BigDecimal previousBalanceAmount(Account account, Date opDate) {
        Optional<Balance> optBalance = txDataManager.load(Balance.class)
                .query("select b from akk$Balance b " +
                        "where b.account.id = :accountId and b.balanceDate <= :balanceDate order by b.balanceDate desc")
                .parameter("accountId", account.getId())
                .parameter("balanceDate", opDate)
                .maxResults(1)
                .optional();
        return optBalance.map(Balance::getAmount).orElse(BigDecimal.ZERO);
    }

    private Date nextBalanceDate(Date opDate) {
        return DateUtils.ceiling(opDate, Calendar.MONTH);
    }

    private void saveUserData(Operation operation) {
        switch (operation.getOpType()) {
            case EXPENSE:
                userDataWorker.saveEntity(UserDataKeys.OP_EXPENSE_ACCOUNT, operation.getAcc1(), false);
                break;
            case INCOME:
                userDataWorker.saveEntity(UserDataKeys.OP_INCOME_ACCOUNT, operation.getAcc2(), false);
                break;
            case TRANSFER:
                userDataWorker.saveEntity(UserDataKeys.OP_TRANSFER_EXPENSE_ACCOUNT, operation.getAcc1(), false);
                userDataWorker.saveEntity(UserDataKeys.OP_TRANSFER_INCOME_ACCOUNT, operation.getAcc2(), false);
                break;
        }
    }
}
