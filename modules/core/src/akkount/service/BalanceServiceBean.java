package akkount.service;

import akkount.entity.Account;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service(BalanceService.NAME)
public class BalanceServiceBean implements BalanceService {

    @Inject
    protected BalanceWorker balanceWorker;
    @Inject
    private DataManager dataManager;

    @Override
    public BigDecimal getBalance(UUID accountId, Date date) {
        return balanceWorker.getBalance(accountId, date);
    }

    @Override
    public void recalculateBalance(UUID accountId) {
        balanceWorker.recalculateBalance(accountId);
    }

    @Override
    public List<BalanceData> getBalanceData(Date date) {
        Map<Integer, List<Account>> accountsByGroup = dataManager.load(Account.class)
                .query("select e from akk_Account e where e.active = true order by e.group")
                .list().stream()
                .collect(Collectors.groupingBy(
                        account -> account.getGroup() != null ? account.getGroup() : 0,
                        TreeMap::new, Collectors.toList()));

        List<BalanceData> result = new ArrayList<>(accountsByGroup.size());

        for (List<Account> accounts : accountsByGroup.values()) {
            Map<Account, BigDecimal> balanceByAccount = new HashMap<>();
            for (Account account : accounts) {
                BigDecimal balance = getBalance(account.getId(), date);
                if (BigDecimal.ZERO.compareTo(balance) != 0) {
                    balanceByAccount.put(account, balance);
                }
            }
            BalanceData balanceData = new BalanceData(balanceByAccount);
            result.add(balanceData);
        }

        return result;
    }
}