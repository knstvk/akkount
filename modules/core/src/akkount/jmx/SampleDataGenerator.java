/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.jmx;

import akkount.entity.*;
import akkount.entity.Currency;
import akkount.service.BalanceWorker;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.app.Authenticated;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("akk_SampleDataGenerator")
public class SampleDataGenerator implements SampleDataGeneratorMBean {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    protected Persistence persistence;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected BalanceWorker balanceWorker;

    private class Context {

        Currency usdCurrency;
        Currency rubCurrency;
        Currency eurCurrency;

        List<Account> accounts = new ArrayList<>();

        List<Category> expenseCategories = new ArrayList<>();

        Category salaryCategory;
        Category otherIncomeCategory;
    }

    private Map<String, BigDecimal> currencyRates = new HashMap<>();

    public SampleDataGenerator() {
        currencyRates.put("rub", BigDecimal.ONE);
        currencyRates.put("usd", new BigDecimal("30"));
        currencyRates.put("eur", new BigDecimal("40"));
    }

    @Override
    @Authenticated
    public String generateSampleData(int numberOfDaysBack) {
        if (numberOfDaysBack < 1 || numberOfDaysBack > 1000) {
            return "numberOfDaysBack must be between 1 and 1000";
        }
        Date startDate = DateUtils.truncate(DateUtils.addDays(timeSource.currentTimestamp(), -numberOfDaysBack),
                Calendar.DAY_OF_MONTH);

        try {
            Context context = new Context();
            createCurrencies(context);
            createAccounts(context);
            createCategories(context);
            createOperations(startDate, numberOfDaysBack, context);

            return "Done";
        } catch (Throwable e) {
            log.error("Error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String removeAllData(String confirm) {
        if (!"ok".equals(confirm)) {
            return "Pass 'ok' in the parameter";
        }

        try {
            cleanupTable("AKK_OPERATION");
            cleanupTable("AKK_BALANCE");
            cleanupTable("AKK_ACCOUNT");
            cleanupTable("AKK_CATEGORY");
            cleanupTable("AKK_CURRENCY");

            return "Done";
        } catch (Throwable e) {
            log.error("Error", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    private void cleanupTable(String table) throws SQLException {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from " + table);
    }

    private void createCurrencies(final Context context) {
        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                Currency currency = new Currency();
                currency.setCode("rub");
                currency.setName("Russian Rubles");
                em.persist(currency);
                context.rubCurrency = currency;

                currency = new Currency();
                currency.setCode("usd");
                currency.setName("US Dollars");
                em.persist(currency);
                context.usdCurrency = currency;

                currency = new Currency();
                currency.setCode("eur");
                currency.setName("Euro");
                em.persist(currency);
                context.eurCurrency = currency;
            }
        });
    }

    private void createAccounts(final Context context) {
        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                Account account;

                account = new Account();
                account.setName("Credit card");
                account.setCurrency(context.rubCurrency);
                em.persist(account);
                context.accounts.add(account);

                account = new Account();
                account.setName("Cash");
                account.setCurrency(context.rubCurrency);
                em.persist(account);
                context.accounts.add(account);

                account = new Account();
                account.setName("Deposit");
                account.setCurrency(context.rubCurrency);
                em.persist(account);
                context.accounts.add(account);

                account = new Account();
                account.setName("Deposit USD");
                account.setCurrency(context.usdCurrency);
                em.persist(account);
                context.accounts.add(account);

                account = new Account();
                account.setName("Deposit EUR");
                account.setCurrency(context.eurCurrency);
                em.persist(account);
                context.accounts.add(account);

            }
        });
    }

    private void createCategories(final Context context) {
        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                Category category;

                category = new Category();
                category.setName("Housekeeping");
                category.setCatType(CategoryType.EXPENSE);
                em.persist(category);
                context.expenseCategories.add(category);

                category = new Category();
                category.setName("Hobby");
                category.setCatType(CategoryType.EXPENSE);
                em.persist(category);
                context.expenseCategories.add(category);

                category = new Category();
                category.setName("Travel");
                category.setCatType(CategoryType.EXPENSE);
                em.persist(category);
                context.expenseCategories.add(category);

                category = new Category();
                category.setName("Food");
                category.setCatType(CategoryType.EXPENSE);
                em.persist(category);
                context.expenseCategories.add(category);

                category = new Category();
                category.setName("Clothes");
                category.setCatType(CategoryType.EXPENSE);
                em.persist(category);
                context.expenseCategories.add(category);

                category = new Category();
                category.setName("Car");
                category.setCatType(CategoryType.EXPENSE);
                em.persist(category);
                context.expenseCategories.add(category);

                category = new Category();
                category.setName("Salary");
                category.setCatType(CategoryType.INCOME);
                em.persist(category);
                context.salaryCategory = category;

                category = new Category();
                category.setName("Other");
                category.setCatType(CategoryType.INCOME);
                em.persist(category);
                context.otherIncomeCategory = category;
            }
        });
    }

    private void createOperations(Date startDate, int numberOfDays, Context context) {
        for (int i = 0; i < numberOfDays; i++) {
            Date date = DateUtils.addDays(startDate, i);

            if (i % 7 == 0) {
                income(date, context.accounts.get(0), context.salaryCategory, new BigDecimal("16000"));
            }
            if (i % 5 == 0) {
                income(date, context.accounts.get(1), context.otherIncomeCategory, new BigDecimal(1000 + Math.round(Math.random() * 5000)));
            }

            for (int j = 0; j < Math.random() * 7; j++) {
                expense(date, context.accounts.get(1), context);
                if (j % 2 == 0)
                    expense(date, context.accounts.get(0), context);
            }

            if (i % 2 == 0) {
                Account account2 = context.accounts.get((int) (1 + Math.random() * (context.accounts.size() - 1)));
                transfer(date, context.accounts.get(0), account2);
            }

        }
    }

    private void income(final Date date, final Account account, final Category category, final BigDecimal amount) {
        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                Operation operation = new Operation();
                operation.setOpType(OperationType.INCOME);
                operation.setOpDate(date);
                operation.setAcc2(account);
                operation.setCategory(category);
                operation.setAmount2(amount);
                em.persist(operation);

                log.info("Income: " + date + ", " + account.getName() + ", " + amount);
            }
        });
    }

    private void expense(final Date date, final Account account, final Context context) {
        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                int categoryIdx = (int) Math.round(Math.random() * (context.expenseCategories.size() - 1));
                Category category = context.expenseCategories.get(categoryIdx);
                if (category == null)
                    return;

                int categoryWeight = context.expenseCategories.size() - categoryIdx;
                BigDecimal amount = randomExpenseAmount(account, date, 0.1 + (categoryWeight * 0.05));
                if (BigDecimal.ZERO.compareTo(amount) >= 0)
                    return;

                Operation operation = new Operation();
                operation.setOpType(OperationType.EXPENSE);
                operation.setOpDate(date);
                operation.setAcc1(account);
                operation.setCategory(category);
                operation.setAmount1(amount);
                em.persist(operation);

                log.info("Expense: " + date + ", " + account.getName() + ", " + amount);
            }
        });
    }

    private void transfer(final Date date, final Account account1, final Account account2) {
        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                BigDecimal amount1 = randomExpenseAmount(account1, date, 0.5);
                if (BigDecimal.ZERO.compareTo(amount1) >= 0)
                    return;

                BigDecimal amount2 = transferAmount(account1, account2, amount1);

                Operation operation = new Operation();
                operation.setOpType(OperationType.TRANSFER);
                operation.setOpDate(date);
                operation.setAcc1(account1);
                operation.setAmount1(amount1);
                operation.setAcc2(account2);
                operation.setAmount2(amount2);
                em.persist(operation);

                log.info("Transfer: " + date + ", " + account1.getName() + ", " + amount1+ ", " + account2.getName() + ", " + amount2);
            }
        });
    }


    private BigDecimal randomExpenseAmount(Account account, Date date, Double part) {
        BigDecimal balance = balanceWorker.getBalance(account.getId(), date);
        if (BigDecimal.ZERO.compareTo(balance) >= 0)
            return BigDecimal.ZERO;
        else {
            return new BigDecimal((int) (Math.random() * balance.doubleValue() * part));
        }
    }

    private BigDecimal transferAmount(Account account1, Account account2, BigDecimal amount1) {
        if (account1.getCurrency().equals(account2.getCurrency()))
            return amount1;

        BigDecimal rate1 = currencyRates.get(account1.getCurrencyCode());
        BigDecimal rate2 = currencyRates.get(account2.getCurrencyCode());
        return amount1.multiply(rate1).divide(rate2, 0, RoundingMode.HALF_UP);
    }
}
