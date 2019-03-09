package akkount.entitylisteners;

import akkount.entity.Account;
import akkount.entity.Balance;
import akkount.entity.Operation;
import akkount.service.UserDataKeys;
import akkount.service.UserDataWorker;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OperationEntityListener implements
        BeforeInsertEntityListener<Operation>,
        BeforeUpdateEntityListener<Operation>,
        BeforeDeleteEntityListener<Operation> {

    private Persistence persistence = AppBeans.get(Persistence.class);

    private Metadata metadata = AppBeans.get(Metadata.class);

    private UserDataWorker userDataWorker = AppBeans.get(UserDataWorker.class);

    private static final String BALANCE_QUERY = "select b from akk$Balance b " +
            "where b.account.id = ?1 and b.balanceDate > ?2 order by b.balanceDate";

    @Override
    public void onBeforeInsert(Operation entity, EntityManager entityManager) {
        addOperation(entity, entityManager);
        saveUserData(entity);
    }

    @Override
    public void onBeforeUpdate(Operation entity, EntityManager entityManager) {
        removeOperation(getOldOperation(entity.getId()), entityManager);
        addOperation(entity, entityManager);
        saveUserData(entity);
    }

    @Override
    public void onBeforeDelete(Operation entity, EntityManager entityManager) {
        removeOperation(entity, entityManager);
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

    private Operation getOldOperation(UUID operationId) {
        Operation operation;
        // Get old operation state in separate transaction
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            operation = em.find(Operation.class, operationId, "operation-with-accounts");

            tx.commit();
        }
        return operation;
    }

    private void removeOperation(Operation operation, EntityManager em) {
        if (operation.getAcc1() != null) {
            TypedQuery<Balance> query = em.createQuery(BALANCE_QUERY, Balance.class);
            query.setParameter(1, operation.getAcc1().getId()).setParameter(2, operation.getOpDate());
            List<Balance> list = query.getResultList();
            if (!list.isEmpty()) {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().add(operation.getAmount1()));
                }
            }
        }

        if (operation.getAcc2() != null) {
            TypedQuery<Balance> query = em.createQuery(BALANCE_QUERY, Balance.class);
            query.setParameter(1, operation.getAcc2().getId()).setParameter(2, operation.getOpDate());
            List<Balance> list = query.getResultList();
            if (!list.isEmpty()) {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().subtract(operation.getAmount2()));
                }
            }
        }
    }

    private void addOperation(Operation operation, EntityManager em) {
        if (operation.getAcc1() != null) {
            TypedQuery<Balance> query = em.createQuery(BALANCE_QUERY, Balance.class);
            query.setParameter(1, operation.getAcc1().getId()).setParameter(2, operation.getOpDate());
            List<Balance> list = query.getResultList();

            if (list.isEmpty()) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc1());
                balance.setAmount(operation.getAmount1().negate()
                        .add(previousBalanceAmount(operation.getAcc1(), operation.getOpDate(), em)));
                balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
                em.persist(balance);
            } else {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().subtract(operation.getAmount1()));
                }
            }
        }

        if (operation.getAcc2() != null) {
            TypedQuery<Balance> query = em.createQuery(BALANCE_QUERY, Balance.class);
            query.setParameter(1, operation.getAcc2().getId()).setParameter(2, operation.getOpDate());
            List<Balance> list = query.getResultList();

            if (list.isEmpty()) {
                Balance balance = metadata.create(Balance.class);
                balance.setAccount(operation.getAcc2());
                balance.setAmount(operation.getAmount2()
                        .add(previousBalanceAmount(operation.getAcc2(), operation.getOpDate(), em)));
                balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
                em.persist(balance);
            } else {
                for (Balance balance : list) {
                    balance.setAmount(balance.getAmount().add(operation.getAmount2()));
                }
            }
        }
    }

    private BigDecimal previousBalanceAmount(Account account, Date opDate, EntityManager em) {
        TypedQuery<Balance> query = em.createQuery("select b from akk$Balance b " +
                "where b.account.id = ?1 and b.balanceDate <= ?2 order by b.balanceDate desc", Balance.class);
        query.setParameter(1, account.getId());
        query.setParameter(2, opDate);
        query.setMaxResults(1);
        Balance prevBalance = query.getFirstResult();
        return prevBalance == null ? BigDecimal.ZERO : prevBalance.getAmount();
    }

    private Date nextBalanceDate(Date opDate) {
        return DateUtils.ceiling(opDate, Calendar.MONTH);
    }
}
