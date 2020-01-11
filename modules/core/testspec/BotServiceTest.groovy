import akkount.AkkTestContainer
import akkount.entity.Account
import akkount.entity.Category
import akkount.entity.CategoryType
import akkount.entity.Currency
import akkount.entity.Operation
import akkount.service.BotService
import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.sys.AppContext
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class BotServiceTest extends Specification {

    @Shared @ClassRule
    AkkTestContainer cont = AkkTestContainer.Common.INSTANCE

    private Metadata metadata
    private Persistence persistence
    private DataManager dataManager
    private BotService botService

    private Currency currency1
    private Account account1
    private Account account2
    private Category category1
    private Category category2

    void setup() {
        metadata = AppBeans.get(Metadata)
        persistence = AppBeans.get(Persistence)
        dataManager = AppBeans.get(DataManager)
        botService = AppBeans.get(BotService)

        cleanup()

        currency1 = metadata.create(Currency)
        currency1.code = 'ZZZ'
        currency1.name = 'Zzzzzz'

        account1 = metadata.create(Account)
        account1.name = 'acc_one'
        account1.currency = this.currency1

        account2 = metadata.create(Account)
        account2.name = 'acc two'
        account2.currency = this.currency1

        category1 = metadata.create(Category)
        category1.name = 'cat_one'
        category1.catType = CategoryType.EXPENSE

        category2 = metadata.create(Category)
        category2.name = 'cat two'
        category2.catType = CategoryType.EXPENSE

        dataManager.commit(currency1)
        dataManager.commit(account1, account2, category1, category2)
    }

    void cleanup() {
        QueryRunner runner = new QueryRunner(persistence.getDataSource())
        runner.update('delete from AKK_OPERATION')
        runner.update('delete from AKK_BALANCE')
        runner.update('delete from AKK_ACCOUNT')
        runner.update('delete from AKK_CATEGORY')
        runner.update('delete from AKK_CURRENCY')
    }

    def "test message with account"() {

        when:

        def reply = botService.processMessage("acc_one 100 cat_one")
        println(reply)

        then:

        def operations = loadOperations()

        operations.size() == 1
        operations[0].amount1 == 100
        operations[0].acc1 == account1
        operations[0].category == category1
    }

    def "test message without account"() {

        AppContext.setProperty('akk.defaultAccount', 'acc_one')

        when:

        def reply = botService.processMessage("100 cat_one")
        println(reply)

        then:

        def operations = loadOperations()

        operations.size() == 1
        operations[0].amount1 == 100
        operations[0].acc1 == account1
        operations[0].category == category1

        cleanup:

        AppContext.setProperty('akk.defaultAccount', null)
    }

    def "test message without category"() {

        AppContext.setProperty('akk.defaultCategory', 'cat_one')

        when:

        def reply = botService.processMessage("acc_one 100")
        println(reply)

        then:

        def operations = loadOperations()

        operations.size() == 1
        operations[0].amount1 == 100
        operations[0].acc1 == account1
        operations[0].category == category1

        cleanup:

        AppContext.setProperty('akk.defaultCategory', null)
    }

    def "test message without account and category"() {

        AppContext.setProperty('akk.defaultAccount', 'acc_one')
        AppContext.setProperty('akk.defaultCategory', 'cat_one')

        when:

        def reply = botService.processMessage("100")
        println(reply)

        then:

        def operations = loadOperations()

        operations.size() == 1
        operations[0].amount1 == 100
        operations[0].acc1 == account1
        operations[0].category == category1

        cleanup:

        AppContext.setProperty('akk.defaultAccount', null)
        AppContext.setProperty('akk.defaultCategory', null)
    }

    def "test message with category having two words"() {

        when:

        def reply = botService.processMessage("acc_one 100 cat two")
        println(reply)

        then:

        def operations = loadOperations()

        operations.size() == 1
        operations[0].amount1 == 100
        operations[0].acc1 == account1
        operations[0].category == category2
    }

    def "test message with account having two words"() {

        when:

        def reply = botService.processMessage("acc two 100 cat two")
        println(reply)

        then:

        def operations = loadOperations()

        operations.size() == 1
        operations[0].amount1 == 100
        operations[0].acc1 == account2
        operations[0].category == category2
    }

    private List<Operation> loadOperations() {
        def query = LoadContext.createQuery('select o from akk_Operation o')
        dataManager.loadList(
                LoadContext.create(Operation).setQuery(query).setView('operation-browse'))
    }
}
