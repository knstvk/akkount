package akkount.service;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service(BalanceService.NAME)
public class BalanceServiceBean implements BalanceService {

    @Inject
    protected BalanceWorker balanceWorker;

    @Override
    public BigDecimal getBalance(UUID accountId, Date date) {
        return balanceWorker.getBalance(accountId, date);
    }
}