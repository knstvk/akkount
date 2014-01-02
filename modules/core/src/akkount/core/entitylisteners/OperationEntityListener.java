/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.core.entitylisteners;

import akkount.entity.Account;
import akkount.entity.Balance;
import akkount.entity.Operation;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class OperationEntityListener
        implements BeforeInsertEntityListener<Operation>, BeforeUpdateEntityListener<Operation> {

    private Persistence persistence = AppBeans.get(Persistence.class);

    private static final String BALANCE_QUERY = "select b from akk$Balance b " +
            "where b.account.id = ?1 and b.balanceDate >= ?2 order by b.balanceDate";

    @Override
    public void onBeforeInsert(Operation entity) {
        updateBalance1(entity);
        updateBalance2(entity);
    }

    @Override
    public void onBeforeUpdate(Operation entity) {
        updateBalance1(entity);
        updateBalance2(entity);
    }

    private void updateBalance1(Operation operation) {
        if (operation.getAcc1() == null)
            return;

        EntityManager em = persistence.getEntityManager();

        TypedQuery<Balance> query = em.createQuery(BALANCE_QUERY, Balance.class);
        query.setParameter(1, operation.getAcc1().getId()).setParameter(2, operation.getOpDate());
        List<Balance> list = query.getResultList();

        if (list.isEmpty()) {
            Balance balance = new Balance();
            balance.setAccount(operation.getAcc1());
            balance.setAmount(operation.getAmount1().negate()
                    .add(previousBalanceAmount(operation.getAcc1(), operation.getOpDate())));
            balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
            em.persist(balance);
        } else {
            for (Balance balance : list) {
                balance.setAmount(balance.getAmount().subtract(operation.getAmount1()));
            }
        }
    }

    private void updateBalance2(Operation operation) {
        if (operation.getAcc2() == null)
            return;

        EntityManager em = persistence.getEntityManager();

        TypedQuery<Balance> query = em.createQuery(BALANCE_QUERY, Balance.class);
        query.setParameter(1, operation.getAcc2().getId()).setParameter(2, operation.getOpDate());
        List<Balance> list = query.getResultList();

        if (list.isEmpty()) {
            Balance balance = new Balance();
            balance.setAccount(operation.getAcc2());
            balance.setAmount(operation.getAmount2()
                    .add(previousBalanceAmount(operation.getAcc2(), operation.getOpDate())));
            balance.setBalanceDate(nextBalanceDate(operation.getOpDate()));
            em.persist(balance);
        } else {
            for (Balance balance : list) {
                balance.setAmount(balance.getAmount().add(operation.getAmount2()));
            }
        }
    }

    private BigDecimal previousBalanceAmount(Account account, Date opDate) {
        Date prevBalanceDate = DateUtils.addMonths(DateUtils.ceiling(opDate, Calendar.MONTH), -1);
        EntityManager em = persistence.getEntityManager();
        TypedQuery<Balance> query = em.createQuery("select b from akk$Balance b " +
                "where b.account.id = ?1 and b.balanceDate = ?2", Balance.class);
        query.setParameter(1, account.getId());
        query.setParameter(2, prevBalanceDate);
        Balance prevBalance = query.getFirstResult();
        return prevBalance == null ? BigDecimal.ZERO : prevBalance.getAmount();
    }

    private Date nextBalanceDate(Date opDate) {
        return DateUtils.ceiling(opDate, Calendar.MONTH);
    }
}
