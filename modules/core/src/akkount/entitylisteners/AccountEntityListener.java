package akkount.entitylisteners;

import akkount.entity.Account;
import akkount.entity.Currency;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;

public class AccountEntityListener
        implements BeforeInsertEntityListener<Account>, BeforeUpdateEntityListener<Account> {

    @Override
    public void onBeforeInsert(Account entity, EntityManager entityManager) {
        setCurrencyCode(entity, entityManager);
    }

    @Override
    public void onBeforeUpdate(Account entity, EntityManager entityManager) {
        setCurrencyCode(entity, entityManager);
    }

    private void setCurrencyCode(Account account, EntityManager entityManager) {
        Currency currency = entityManager.find(Currency.class, account.getCurrency().getId());
        if (currency == null) {
            throw new RuntimeException("Currency not found: " + account.getCurrency());
        }
        account.setCurrencyCode(currency.getCode());
    }
}
