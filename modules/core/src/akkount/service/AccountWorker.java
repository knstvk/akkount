package akkount.service;

import akkount.entity.Account;
import akkount.entity.Currency;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.app.events.EntityPersistingEvent;
import com.haulmont.cuba.core.entity.contracts.Id;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;

@Component("akk_AccountWorker")
public class AccountWorker {

    @Inject
    private TransactionalDataManager tdm;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAccountBeforeCommit(EntityChangedEvent<Account, UUID> event) {
        if (event.getType() == EntityChangedEvent.Type.DELETED)
            return;

        Account account = tdm.load(event.getEntityId()).view("account-with-currency").one();
        if (!Objects.equals(account.getCurrencyCode(), account.getCurrency().getCode())) {
            account.setCurrencyCode(account.getCurrency().getCode());
            tdm.save(account);
        }
    }

    @EventListener
    public void onAccountPersisting(EntityPersistingEvent<Account> event) {
        Account account = event.getEntity();
        Currency currency = tdm.load(Id.of(account.getCurrency())).one();
        account.setCurrencyCode(currency.getCode());
    }

}
