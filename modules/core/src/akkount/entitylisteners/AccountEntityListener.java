package akkount.entitylisteners;

import akkount.entity.Account;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.core.EntityManager;

public class AccountEntityListener
        implements BeforeInsertEntityListener<Account>, BeforeUpdateEntityListener<Account> {

    @Override
    public void onBeforeInsert(Account entity, EntityManager entityManager) {
        setCurrencyCode(entity);
    }

    @Override
    public void onBeforeUpdate(Account entity, EntityManager entityManager) {
        setCurrencyCode(entity);
    }

    private void setCurrencyCode(Account account) {
        account.setCurrencyCode(account.getCurrency().getCode());
    }
}
