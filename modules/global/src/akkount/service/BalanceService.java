package akkount.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface BalanceService {
    String NAME = "akk_BalanceService";

    BigDecimal getBalance(UUID accountId, Date date);

    void recalculateBalance(UUID accountId);

    List<BalanceData> getBalanceData(Date date);

}