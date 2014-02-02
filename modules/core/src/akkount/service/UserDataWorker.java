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
import java.util.ArrayList;
import java.util.List;
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
        List<String> values = getValues(key);
        if (values.isEmpty())
            return null;

        String value = values.get(0);

        return getEntity(value, entityClass);
    }

    public <T extends Entity> List<T> loadEntityList(String key, Class<T> entityClass) {
        ArrayList<T> result = new ArrayList<>();

        List<String> values = getValues(key);
        if (values.isEmpty())
            return result;

        for (String value : values) {
            T entity = getEntity(value, entityClass);
            if (entity != null) {
                result.add(entity);
            }
        }

        return result;
    }

    public void saveEntity(String key, Entity entity, boolean multipleValues) {
        String value = entity.getId().toString();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            String queryString = "select d from akk$UserData d where d.user.id = ?1 and d.key = ?2";
            if (multipleValues)
                queryString += " and d.value = ?3";

            TypedQuery<UserData> query = em.createQuery(
                    queryString, UserData.class);

            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            if (multipleValues)
                query.setParameter(3, value);

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

    public void removeEntity(String key, Entity entity) {
        String value = entity.getId().toString();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<UserData> query = em.createQuery(
                    "select d from akk$UserData d where d.user.id = ?1 and d.key = ?2 and d.value = ?3", UserData.class);
            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            query.setParameter(3, value);
            UserData userData = query.getFirstResult();

            if (userData != null) {
                em.remove(userData);
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private <T extends Entity> T getEntity(String value, Class<T> entityClass) {
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

    private List<String> getValues(String key) {
        List<String> list;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select d.value from akk$UserData d where d.user.id = ?1 and d.key = ?2");
            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            list = query.getResultList();
            tx.commit();
        } finally {
            tx.end();
        }
        return list;
    }
}
