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

    protected UUID account1Id;
    protected UUID account2Id;

    @Override
    protected void initDataSources() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        TestDataSource ds = new TestDataSource("jdbc:hsqldb:hsql://localhost:9002/akk_test", "sa", "");
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
            Currency currency1 = new Currency();
            currency1.setCode("TST");
            currency1.setName("Test Currency");
            persistence.getEntityManager().persist(currency1);

            Account account1 = new Account();
            account1.setCurrency(currency1);
            account1.setName("TestAccount1");
            persistence.getEntityManager().persist(account1);
            account1Id = account1.getId();

            Account account2 = new Account();
            account2.setCurrency(currency1);
            account2.setName("TestAccount2");
            persistence.getEntityManager().persist(account2);
            account2Id = account2.getId();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void cleanupTable(String table) throws SQLException {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from " + table);
    }
}
