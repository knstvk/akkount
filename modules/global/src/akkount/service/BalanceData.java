package akkount.service;

import akkount.entity.Account;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class BalanceData implements Serializable {

    public final List<AccountBalance> totals;
    public final List<AccountBalance> accounts;

    public static class AccountBalance implements Serializable {
        public final String name;
        public final String currency;
        public final BigDecimal amount;

        public AccountBalance(@Nullable String name, String currency, BigDecimal amount) {
            this.name = name;
            this.currency = currency;
            this.amount = amount;
        }
    }

    public BalanceData(Map<Account, BigDecimal> balanceByAccount) {
        accounts = new ArrayList<>();
        Map<String, BigDecimal> balanceByCurrency = new TreeMap<>();

        for (Map.Entry<Account, BigDecimal> entry : balanceByAccount.entrySet()) {
            Account account = entry.getKey();
            accounts.add(new AccountBalance(account.getName(), account.getCurrencyCode(), entry.getValue()));

            BigDecimal val = balanceByCurrency.computeIfAbsent(account.getCurrencyCode(), s -> BigDecimal.ZERO);
            balanceByCurrency.put(account.getCurrencyCode(), val.add(entry.getValue()));
        }

        totals = balanceByCurrency.entrySet().stream()
                .map(entry -> new AccountBalance(null, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
