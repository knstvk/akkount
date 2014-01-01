/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.testsupport;

import akkount.entity.Account;
import akkount.entity.Currency;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.testsupport.TestContext;
import com.haulmont.cuba.testsupport.TestDataSource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AkkountTestCase extends CubaTestCase {

    public static final String TEST_CURRENCY_CODE = "TST";
    public static final String TEST_ACC_NAME = "TestAcc";

    protected UUID accountId;

    @Override
    protected void initDataSources() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        TestDataSource ds = new TestDataSource("jdbc:hsqldb:hsql://localhost/akk_test", "sa", "");
        TestContext.getInstance().bind("java:comp/env/jdbc/CubaDS", ds);
    }

    @Override
    protected List<String> getTestAppProperties() {
        String[] files = {
                "cuba-app.properties",
                "app.properties",
                "test-app.properties",
        };
        return Arrays.asList(files);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cleanupTable("AKK_OPERATION");
        cleanupTable("AKK_BALANCE");
        cleanupTable("AKK_ACCOUNT");
        cleanupTable("AKK_CATEGORY");
        cleanupTable("AKK_CURRENCY");
        initTestData();
    }

    private void initTestData() {
        Transaction tx = persistence.createTransaction();
        try {
            Currency currency = createCurrency();
            createAccount(currency);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private Currency createCurrency() {
        Currency currency = new Currency();
        currency.setCode(TEST_CURRENCY_CODE);
        currency.setName("Test Currency");
        persistence.getEntityManager().persist(currency);
        return currency;
    }

    private Account createAccount(Currency currency) {
        Account account = new Account();
        accountId = account.getId();
        account.setCurrency(currency);
        account.setName(TEST_ACC_NAME);
        persistence.getEntityManager().persist(account);
        return account;
    }

    protected void cleanupTable(String table) throws SQLException {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from " + table);
    }
}
