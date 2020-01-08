package akkount.service;

import akkount.entity.Account;
import akkount.entity.Balance;
import akkount.entity.Operation;
import akkount.event.BalanceChangedEvent;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.AttributeChanges;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

import static com.haulmont.cuba.core.app.events.EntityChangedEvent.Type.DELETED;
import static com.haulmont.cuba.core.app.events.EntityChangedEvent.Type.UPDATED;

@Component(OperationWorker.NAME)
public class OperationWorker {

    public static final String NAME = "akk_OperationWorker";

    @Inject
    private Metadata metadata;

    @Inject
    private TransactionalDataManager tdm;

    @Inject
    private UserDataWorker userDataWorker;

    @Inject
    private Events events;

    private volatile boolean balanceChangedEventsEnabled = true;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOperationChanged(EntityChangedEvent<Operation, UUID> event) {
        AttributeChanges changes = event.getChanges();
        if (event.getType() == DELETED) {
            removeOperation(
                    changes.getOldValue("opDate"),
                    changes.getOldReferenceId("acc1"),
                    changes.getOldReferenceId("acc2"),
                    changes.getOldValue("amount1"),
                    changes.getOldValue("amount2")
            );
        } else {
            Operation operation = tdm.load(event.getEntityId()).view("operation-with-accounts").one();
            if (event.getType() == UPDATED) {
                removeOperation(
                        changes.isChanged("opDate") ? changes.getOldValue("opDate") : operation.getOpDate(),
                        changes.isChanged("acc1") ? changes.getOldReferenceId("acc1") : idOfNullable(operation.getAcc1()),
                        changes.isChanged("acc2") ? changes.getOldReferenceId("acc2") : idOfNullable(operation.getAcc2()),
                        changes.isChanged("amount1") ? changes.getOldValue("amount1") : operation.getAmount1(),
                        changes.isChanged("amount2") ? changes.getOldValue("amount2") : operation.getAmount2()
                );
            }
            addOperation(operation);
            saveUserData(operation);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOperationChangedAndCommitted(EntityChangedEvent<Operation, UUID> event) {
        if (balanceChangedEventsEnabled) {
            events.publish(new BalanceChangedEvent(this));
        }
    }

    public void enableBalanceChangedEvents(boolean enable) {
        balanceChangedEventsEnabled = enable;
    }

    private void removeOperation(Date opDate, Id<Account, UUID> acc1Id, Id<Account, UUID> acc2Id,
                                 BigDecimal amount1, BigDecimal amount2) {
        if (acc1Id != null) {
            List<Balance> list = getBalanceRecords(opDate, acc1Id);
            if (!list.isEmpty()) {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().add(amount1));
                    tdm.save(balance);
                }
            }
        }

        if (acc2Id != null) {
            List<Balance> list = getBalanceRecords(opDate, acc2Id);
            if (!list.isEmpty()) {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().subtract(amount2));
                    tdm.save(balance);
                }
            }
        }
    }

    private void addOperation(Operation operation) {
        if (operation.getAcc1() != null) {
            List<Balance> list = getBalanceRecords(operation.getOpDate(), Id.of(operation.getAcc1()));
            if (list.isEmpty()) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc1());
                balance.setAmount(operation.getAmount1().negate()
                        .add(previousBalanceAmount(operation.getAcc1(), operation.getOpDate())));
                balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
                tdm.save(balance);
            } else {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().subtract(operation.getAmount1()));
                    tdm.save(balance);
                }
            }
        }

        if (operation.getAcc2() != null) {
            List<Balance> list = getBalanceRecords(operation.getOpDate(), Id.of(operation.getAcc2()));
            if (list.isEmpty()) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc2());
                balance.setAmount(operation.getAmount2()
                        .add(previousBalanceAmount(operation.getAcc2(), operation.getOpDate())));
                balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
                tdm.save(balance);
            } else {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().add(operation.getAmount2()));
                    tdm.save(balance);
                }
            }
        }
    }

    private List<Balance> getBalanceRecords(Date opDate, Id<Account, UUID> accId) {
        return tdm.load(Balance.class)
                .query("select b from akk$Balance b " +
                        "where b.account.id = :accountId and b.balanceDate > :balanceDate order by b.balanceDate")
                .parameter("accountId", accId.getValue())
                .parameter("balanceDate", opDate)
                .list();
    }

    private BigDecimal previousBalanceAmount(Account account, Date opDate) {
        Optional<Balance> optBalance = tdm.load(Balance.class)
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

    @Nullable
    private <T extends Entity<K>,K> Id<T,K> idOfNullable(@Nullable T entity) {
        return entity == null ? null : Id.of(entity);
    }
}
