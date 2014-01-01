/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.service;

import akkount.entity.Account;
import akkount.entity.Balance;
import akkount.entity.Operation;
import akkount.entity.OperationType;
import akkount.testsupport.AkkountTestCase;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
public class BalanceServiceBeanTest extends AkkountTestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    private Date date(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void income(Date day, BigDecimal amount) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = new Operation();
            operation.setOpType(OperationType.INCOME);
            operation.setOpDate(day);
            operation.setAcc2(em.getReference(Account.class, accountId));
            operation.setAmount2(amount);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void expense(Date day, BigDecimal amount) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = new Operation();
            operation.setOpType(OperationType.INCOME);
            operation.setOpDate(day);
            operation.setAcc1(em.getReference(Account.class, accountId));
            operation.setAmount1(amount);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void checkBalanceRecord(Date day, BigDecimal amount) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<Balance> query = em.createQuery(
                    "select b from akk$Balance b where b.account.id = ?1 and b.balanceDate = ?2", Balance.class);
            query.setParameter(1, accountId);
            query.setParameter(2, day);
            Balance balance = query.getFirstResult();
            assertNotNull("Balance record doesn't exist", balance);
            assertTrue(amount.compareTo(balance.getAmount()) == 0);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testGetBalance() throws Exception {
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        BigDecimal balance = balanceService.getBalance(accountId, date("2014-01-01"));
        assertEquals(BigDecimal.ZERO, balance);

        income(date("2014-01-01"), BigDecimal.TEN);
        checkBalanceRecord(date("2014-02-01"), BigDecimal.TEN);

        balance = balanceService.getBalance(accountId, date("2014-01-02"));
        assertTrue(BigDecimal.TEN.compareTo(balance) == 0);

        balance = balanceService.getBalance(accountId, date("2014-02-01"));
        assertTrue(BigDecimal.TEN.compareTo(balance) == 0);

        balance = balanceService.getBalance(accountId, date("2014-02-02"));
        assertTrue(BigDecimal.TEN.compareTo(balance) == 0);

        expense(date("2014-01-01"), BigDecimal.ONE);

        balance = balanceService.getBalance(accountId, date("2014-01-02"));
        assertTrue(new BigDecimal("9").compareTo(balance) == 0);

        balance = balanceService.getBalance(accountId, date("2014-02-01"));
        assertTrue(new BigDecimal("9").compareTo(balance) == 0);

        balance = balanceService.getBalance(accountId, date("2014-02-02"));
        assertTrue(new BigDecimal("9").compareTo(balance) == 0);

    }
}
