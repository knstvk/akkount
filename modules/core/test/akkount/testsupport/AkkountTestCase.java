package akkount.testsupport;

import akkount.AkkTestContainer;
import akkount.entity.Account;
import akkount.entity.Currency;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import org.junit.Before;
import org.junit.ClassRule;

import java.sql.SQLException;
import java.util.UUID;

public class AkkountTestCase {

    @ClassRule
    public static AkkTestContainer cont = AkkTestContainer.Common.INSTANCE;

    protected UUID account1Id;
    protected UUID account2Id;

    protected DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);

        cleanupTable("AKK_OPERATION");
        cleanupTable("AKK_BALANCE");
        cleanupTable("AKK_ACCOUNT");
        cleanupTable("AKK_CATEGORY");
        cleanupTable("AKK_CURRENCY");
        initTestData();
    }

    private void initTestData() {
        Currency currency1 = cont.metadata().create(Currency.class);
        currency1.setCode("TST");
        currency1.setName("Test Currency");
        dataManager.commit(currency1);

        Account account1 = cont.metadata().create(Account.class);
        account1.setCurrency(currency1);
        account1.setName("TestAccount1");
        dataManager.commit(account1);
        account1Id = account1.getId();

        Account account2 = cont.metadata().create(Account.class);
        account2.setCurrency(currency1);
        account2.setName("TestAccount2");
        dataManager.commit(account2);
        account2Id = account2.getId();
    }

    protected void cleanupTable(String table) throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from " + table);
    }
}
