/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.service;

import akkount.entity.UserData;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(UserDataWorker.NAME)
public class UserDataWorker {

    public static final String NAME = "akk_UserDataWorker";

    private Log log = LogFactory.getLog(UserDataWorker.class);

    @Inject
    protected Persistence persistence;

    @Inject
    protected UserSessionSource userSessionSource;

    public <T extends Entity> T loadEntity(String key, Class<T> entityClass) {
        String value = getValue(key);
        if (value == null)
            return null;

        UUID entityId;
        try {
            entityId = UUID.fromString(value);
        } catch (Exception e) {
            log.warn("Invalid entity ID: " + value);
            return null;
        }

        T entity;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            entity = em.find(entityClass, entityId);
            tx.commit();
        } finally {
            tx.end();
        }
        return entity;
    }

    public void saveEntity(String key, Entity entity) {
        String value = entity.getId().toString();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<UserData> query = em.createQuery(
                    "select d from akk$UserData d where d.user.id = ?1 and d.key = ?2", UserData.class);
            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            UserData userData = query.getFirstResult();

            if (userData == null) {
                userData = new UserData();
                userData.setUser(em.getReference(User.class, userSessionSource.currentOrSubstitutedUserId()));
                userData.setKey(key);
                userData.setValue(value);
                em.persist(userData);
            } else {
                userData.setValue(value);
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private String getValue(String key) {
        String value;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select d.value from akk$UserData d where d.user.id = ?1 and d.key = ?2");
            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            value = (String) query.getFirstResult();
            tx.commit();
        } finally {
            tx.end();
        }
        return value;
    }
}
