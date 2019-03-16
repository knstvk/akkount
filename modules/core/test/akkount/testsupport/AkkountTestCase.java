package akkount.testsupport;

import akkount.AkkTestContainer;
import akkount.entity.Account;
import akkount.entity.Currency;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.Transaction;
import org.junit.Before;
import org.junit.ClassRule;

import java.sql.SQLException;
import java.util.UUID;

public class AkkountTestCase {

    @ClassRule
    public static AkkTestContainer cont = AkkTestContainer.Common.INSTANCE;

    protected UUID account1Id;
    protected UUID account2Id;

    @Before
    public void setUp() throws Exception {
        cleanupTable("AKK_OPERATION");
        cleanupTable("AKK_BALANCE");
        cleanupTable("AKK_ACCOUNT");
        cleanupTable("AKK_CATEGORY");
        cleanupTable("AKK_CURRENCY");
        initTestData();
    }

    private void initTestData() {
        try (Transaction tx = cont.persistence().createTransaction()) {
            Currency currency1 = new Currency();
            currency1.setCode("TST");
            currency1.setName("Test Currency");
            cont.persistence().getEntityManager().persist(currency1);

            Account account1 = new Account();
            account1.setCurrency(currency1);
            account1.setName("TestAccount1");
            cont.persistence().getEntityManager().persist(account1);
            account1Id = account1.getId();

            Account account2 = new Account();
            account2.setCurrency(currency1);
            account2.setName("TestAccount2");
            cont.persistence().getEntityManager().persist(account2);
            account2Id = account2.getId();

            tx.commit();
        }
    }

    protected void cleanupTable(String table) throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from " + table);
    }
}
