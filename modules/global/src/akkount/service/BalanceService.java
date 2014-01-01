package akkount.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public interface BalanceService {
    String NAME = "akk_BalanceService";

    BigDecimal getBalance(UUID accountId, Date date);
}