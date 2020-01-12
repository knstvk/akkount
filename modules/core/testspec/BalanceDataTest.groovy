import akkount.entity.Account
import akkount.entity.Currency
import akkount.service.BalanceData
import spock.lang.Specification

class BalanceDataTest extends Specification {

    def "create from list of accounts"() {

        def eur = new Currency(code: 'eur', name: 'Euro')
        def usd = new Currency(code: 'usd', name: 'USD')

        def acc1 = new Account(name: 'acc1', currency: eur, currencyCode: eur.code)
        def acc2 = new Account(name: 'acc2', currency: usd, currencyCode: usd.code)
        def acc3 = new Account(name: 'acc2', currency: eur, currencyCode: eur.code)

        def group = [(acc1): 10.0, (acc2): 20.0, (acc3): 30.0]

        when:
        def balanceData = new BalanceData(group)

        then:
        balanceData.accounts.size() == 3

        balanceData.accounts[0].name == acc1.name
        balanceData.accounts[0].currency == 'eur'
        balanceData.accounts[0].amount == 10

        balanceData.accounts[1].name == acc2.name
        balanceData.accounts[1].currency == 'usd'
        balanceData.accounts[1].amount == 20

        balanceData.accounts[2].name == acc3.name
        balanceData.accounts[2].currency == 'eur'
        balanceData.accounts[2].amount == 30

        balanceData.totals.size() == 2

        balanceData.totals[0].currency == 'eur'
        balanceData.totals[0].amount == 40

        balanceData.totals[1].currency == 'usd'
        balanceData.totals[1].amount == 20
    }
}
