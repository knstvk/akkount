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
import java.util.UUID;

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

    private UUID income(Date day, BigDecimal amount) {
        UUID operationId;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = new Operation();
            operationId = operation.getId();
            operation.setOpType(OperationType.INCOME);
            operation.setOpDate(day);
            operation.setAcc2(em.getReference(Account.class, account1Id));
            operation.setAmount2(amount);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
        return operationId;
    }

    private UUID expense(Date day, BigDecimal amount) {
        UUID operationId;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = new Operation();
            operationId = operation.getId();
            operation.setOpType(OperationType.EXPENSE);
            operation.setOpDate(day);
            operation.setAcc1(em.getReference(Account.class, account1Id));
            operation.setAmount1(amount);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
        return operationId;
    }

    private UUID transfer(Date day, BigDecimal amount) {
        UUID operationId;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = new Operation();
            operationId = operation.getId();
            operation.setOpType(OperationType.INCOME);
            operation.setOpDate(day);
            operation.setAcc1(em.getReference(Account.class, account1Id));
            operation.setAmount1(amount);
            operation.setAcc2(em.getReference(Account.class, account2Id));
            operation.setAmount2(amount);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
        return operationId;
    }

    private void expenseUpdate(UUID operationId, Date day, BigDecimal amount) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = em.find(Operation.class, operationId);
            assertNotNull(operation);
            operation.setOpType(OperationType.EXPENSE);
            operation.setOpDate(day);
            operation.setAcc1(em.getReference(Account.class, account1Id));
            operation.setAcc2(null);
            operation.setAmount1(amount);
            operation.setAmount2(BigDecimal.ZERO);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void incomeUpdate(UUID operationId, Date day, BigDecimal amount) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Operation operation = em.find(Operation.class, operationId);
            assertNotNull(operation);
            operation.setOpType(OperationType.INCOME);
            operation.setOpDate(day);
            operation.setAcc1(null);
            operation.setAcc2(em.getReference(Account.class, account1Id));
            operation.setAmount1(BigDecimal.ZERO);
            operation.setAmount2(amount);

            em.persist(operation);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void removeOperation(UUID operationId) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Operation operation = em.find(Operation.class, operationId);
            em.remove(operation);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private static void checkEquality(BigDecimal expected, BigDecimal actual) {
        assertTrue(String.format("%s expected, %s actual", expected.toString(), actual.toString()),
                expected.compareTo(actual) == 0);
    }

    private void checkBalanceRecord(Date day, BigDecimal amount) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<Balance> query = em.createQuery(
                    "select b from akk$Balance b where b.account.id = ?1 and b.balanceDate = ?2", Balance.class);
            query.setParameter(1, account1Id);
            query.setParameter(2, day);
            Balance balance = query.getFirstResult();
            assertNotNull("Balance record doesn't exist", balance);
            checkEquality(amount, balance.getAmount());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    public void testGetBalance() throws Exception {
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        BigDecimal balance = balanceService.getBalance(account1Id, date("2014-01-01"));
        assertEquals(BigDecimal.ZERO, balance);

        ///////////////////////////////////////////////////

        income(date("2014-01-01"), BigDecimal.TEN);

        checkBalanceRecord(date("2014-02-01"), BigDecimal.TEN);

        balance = balanceService.getBalance(account1Id, date("2014-01-02"));
        checkEquality(BigDecimal.TEN, balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(BigDecimal.TEN, balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(BigDecimal.TEN, balance);

        ///////////////////////////////////////////////////

        UUID expenseId = expense(date("2014-01-01"), BigDecimal.ONE);

        checkBalanceRecord(date("2014-02-01"), new BigDecimal("9"));

        balance = balanceService.getBalance(account1Id, date("2014-01-02"));
        checkEquality(new BigDecimal("9"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(new BigDecimal("9"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("9"), balance);

        ///////////////////////////////////////////////////

        UUID incomeId = income(date("2014-02-05"), BigDecimal.TEN);

        checkBalanceRecord(date("2014-03-01"), new BigDecimal("19"));

        balance = balanceService.getBalance(account1Id, date("2014-02-04"));
        checkEquality(new BigDecimal("9"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-05"));
        checkEquality(new BigDecimal("19"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-04-10"));
        checkEquality(new BigDecimal("19"), balance);

        ///////////////////////////////////////////////////

        removeOperation(incomeId);

        checkBalanceRecord(date("2014-03-01"), new BigDecimal("9"));

        balance = balanceService.getBalance(account1Id, date("2014-02-05"));
        checkEquality(new BigDecimal("9"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-04-10"));
        checkEquality(new BigDecimal("9"), balance);

        ///////////////////////////////////////////////////

        expenseUpdate(expenseId, date("2014-01-01"), new BigDecimal("2"));

        checkBalanceRecord(date("2014-02-01"), new BigDecimal("8"));
        checkBalanceRecord(date("2014-03-01"), new BigDecimal("8"));

        balance = balanceService.getBalance(account1Id, date("2014-01-02"));
        checkEquality(new BigDecimal("8"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(new BigDecimal("8"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("8"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-04-10"));
        checkEquality(new BigDecimal("8"), balance);

        ///////////////////////////////////////////////////

        incomeUpdate(expenseId, date("2014-01-01"), new BigDecimal("20"));

        checkBalanceRecord(date("2014-02-01"), new BigDecimal("30"));
        checkBalanceRecord(date("2014-03-01"), new BigDecimal("30"));

        balance = balanceService.getBalance(account1Id, date("2014-01-02"));
        checkEquality(new BigDecimal("30"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(new BigDecimal("30"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("30"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-04-10"));
        checkEquality(new BigDecimal("30"), balance);

        ///////////////////////////////////////////////////

        incomeUpdate(expenseId, date("2014-02-02"), new BigDecimal("20"));

        checkBalanceRecord(date("2014-02-01"), new BigDecimal("10"));
        checkBalanceRecord(date("2014-03-01"), new BigDecimal("30"));

        balance = balanceService.getBalance(account1Id, date("2014-01-02"));
        checkEquality(new BigDecimal("10"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(new BigDecimal("10"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("30"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-04-10"));
        checkEquality(new BigDecimal("30"), balance);
    }

    public void testOperationOnFirstDayOfMonth() {
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        BigDecimal balance;
        balance = balanceService.getBalance(account1Id, date("2014-01-01"));
        checkEquality(BigDecimal.ZERO, balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(BigDecimal.ZERO, balance);

        ///////////////////////////////////////////////////

        income(date("2014-01-10"), BigDecimal.TEN);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(BigDecimal.TEN, balance);

        ///////////////////////////////////////////////////

        expense(date("2014-01-30"), BigDecimal.ONE);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("9"), balance);

        ///////////////////////////////////////////////////

        expense(date("2014-02-01"), BigDecimal.ONE);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("8"), balance);

        ///////////////////////////////////////////////////

        expense(date("2014-02-02"), BigDecimal.ONE);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("7"), balance);
    }

    public void testTransfer() throws Exception {
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        BigDecimal balance;

        ///////////////////////////////////////////////////

        income(date("2014-01-10"), BigDecimal.TEN);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(BigDecimal.TEN, balance);

        ///////////////////////////////////////////////////

        transfer(date("2014-01-11"), BigDecimal.ONE);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("9"), balance);

        balance = balanceService.getBalance(account2Id, date("2014-02-02"));
        checkEquality(BigDecimal.ONE, balance);
    }
}
