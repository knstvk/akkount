package akkount.service;

import akkount.entity.Account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface PortalService {
    String NAME = "akk_PortalService";

    Account getLastAccount(String opType);

    Balance getBalance();

    class Balance implements Serializable {
        public List<AccountBalance> excludedAccounts;
        public List<AccountBalance> includedAccounts;
        public List<AccountBalance> totals;

        public static class AccountBalance implements Serializable {
            public String amount;
            public String name;
            public String currency;
        }
    }
}