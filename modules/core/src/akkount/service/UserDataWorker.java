package akkount.service;

import akkount.entity.UserData;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component(UserDataWorker.NAME)
public class UserDataWorker {

    public static final String NAME = "akk_UserDataWorker";

    private Log log = LogFactory.getLog(UserDataWorker.class);

    @Inject
    protected Persistence persistence;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    private Metadata metadata;

    @Nullable
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
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            String queryString = "select d from akk_UserData d where d.user.id = ?1 and d.key = ?2";
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
                userData = metadata.create(UserData.class);
                userData.setUser(em.getReference(User.class, userSessionSource.currentOrSubstitutedUserId()));
                userData.setKey(key);
                userData.setValue(value);
                em.persist(userData);
            } else {
                userData.setValue(value);
            }

            tx.commit();
        }
    }

    public void removeEntity(String key, Entity entity) {
        String value = entity.getId().toString();
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<UserData> query = em.createQuery(
                    "select d from akk_UserData d where d.user.id = ?1 and d.key = ?2 and d.value = ?3", UserData.class);
            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            query.setParameter(3, value);
            UserData userData = query.getFirstResult();

            if (userData != null) {
                em.remove(userData);
            }

            tx.commit();
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
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            //noinspection unchecked
            entity = (T) em.find((Class<Entity<UUID>>) entityClass, entityId);
            tx.commit();
        }
        return entity;
    }

    private List<String> getValues(String key) {
        List<String> list;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<String> query = em.createQuery(
                    "select d.value from akk_UserData d where d.user.id = ?1 and d.key = ?2", String.class);
            query.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            query.setParameter(2, key);
            list = query.getResultList();
            tx.commit();
        }
        return list;
    }
}
