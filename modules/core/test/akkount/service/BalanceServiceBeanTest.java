package akkount.service;

import akkount.entity.Account;
import akkount.entity.Balance;
import akkount.entity.Operation;
import akkount.entity.OperationType;
import akkount.testsupport.AkkountTestCase;
import com.haulmont.cuba.core.global.AppBeans;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BalanceServiceBeanTest extends AkkountTestCase {

    private Date date(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private UUID income(Date day, BigDecimal amount) {
        return income(day, amount, account1Id);
    }

    private UUID income(Date day, BigDecimal amount, UUID accountId) {
        Operation operation = cont.metadata().create(Operation.class);
        operation.setOpType(OperationType.INCOME);
        operation.setOpDate(day);
        operation.setAcc2(dataManager.getReference(Account.class, accountId));
        operation.setAmount2(amount);
        dataManager.commit(operation);
        return operation.getId();
    }

    private UUID expense(Date day, BigDecimal amount) {
        Operation operation = cont.metadata().create(Operation.class);
        operation.setOpType(OperationType.EXPENSE);
        operation.setOpDate(day);
        operation.setAcc1(dataManager.getReference(Account.class, account1Id));
        operation.setAmount1(amount);
        dataManager.commit(operation);
        return operation.getId();
    }

    private UUID transfer(Date day, BigDecimal amount) {
        Operation operation = cont.metadata().create(Operation.class);
        operation.setOpType(OperationType.INCOME);
        operation.setOpDate(day);
        operation.setAcc1(dataManager.getReference(Account.class, account1Id));
        operation.setAmount1(amount);
        operation.setAcc2(dataManager.getReference(Account.class, account2Id));
        operation.setAmount2(amount);
        dataManager.commit(operation);
        return operation.getId();
    }

    private void expenseUpdate(UUID operationId, Date day, BigDecimal amount) {
        Operation operation = dataManager.load(Operation.class).id(operationId).view("operation-with-accounts").one();
        operation.setOpType(OperationType.EXPENSE);
        operation.setOpDate(day);
        operation.setAcc1(dataManager.getReference(Account.class, account1Id));
        operation.setAcc2(null);
        operation.setAmount1(amount);
        operation.setAmount2(BigDecimal.ZERO);
        dataManager.commit(operation);
    }

    private void incomeUpdate(UUID operationId, Date day, BigDecimal amount) {
        Operation operation = dataManager.load(Operation.class).id(operationId).view("operation-with-accounts").one();
        operation.setOpType(OperationType.INCOME);
        operation.setOpDate(day);
        operation.setAcc1(null);
        operation.setAcc2(dataManager.getReference(Account.class, account1Id));
        operation.setAmount1(BigDecimal.ZERO);
        operation.setAmount2(amount);
        dataManager.commit(operation);
    }

    private void removeOperation(UUID operationId) {
        Operation operation = dataManager.load(Operation.class).id(operationId).one();
        dataManager.remove(operation);
    }

    private static void checkEquality(BigDecimal expected, BigDecimal actual) {
        assertTrue(String.format("%s expected, %s actual", expected.toString(), actual.toString()),
                expected.compareTo(actual) == 0);
    }

    private void checkBalanceRecord(Date day, BigDecimal amount) {
        Balance balance = dataManager.load(Balance.class)
                .query("select b from akk_Balance b where b.account.id = :accId and b.balanceDate = :balDate")
                .parameter("accId", account1Id)
                .parameter("balDate", day)
                .one();
        checkEquality(amount, balance.getAmount());
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
    public void testMissedMonths() throws Exception {
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        BigDecimal balance;

        ///////////////////////////////////////////////////

        income(date("2014-01-31"), BigDecimal.TEN);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(BigDecimal.TEN, balance);

        ///////////////////////////////////////////////////

        transfer(date("2014-05-07"), BigDecimal.TEN);

        balance = balanceService.getBalance(account1Id, date("2014-06-02"));
        checkEquality(BigDecimal.ZERO, balance);
    }

    @Test
    public void testRecalculateBalance() throws Exception {
        testGetBalance();

        BalanceService balanceService = AppBeans.get(BalanceService.class);

        balanceService.recalculateBalance(account1Id);

        BigDecimal balance = balanceService.getBalance(account1Id, date("2014-01-02"));
        checkEquality(new BigDecimal("10"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-01"));
        checkEquality(new BigDecimal("10"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-02-02"));
        checkEquality(new BigDecimal("30"), balance);

        balance = balanceService.getBalance(account1Id, date("2014-04-10"));
        checkEquality(new BigDecimal("30"), balance);
    }

    @Test
    public void testGetBalanceData() {
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        income(date("2020-01-01"), BigDecimal.ONE, account1Id);
        income(date("2020-01-02"), BigDecimal.TEN, account2Id);

        List<BalanceData> balanceData = balanceService.getBalanceData(date("2020-01-03"));
        assertEquals(2, balanceData.size());

        List<BalanceData.AccountBalance> totals1 = balanceData.get(0).totals;
        assertEquals(1, totals1.size());
        assertEquals(0, BigDecimal.ONE.compareTo(totals1.get(0).amount));

        List<BalanceData.AccountBalance> totals2 = balanceData.get(1).totals;
        assertEquals(1, totals2.size());
        assertEquals(0, BigDecimal.TEN.compareTo(totals2.get(0).amount));
    }
}
