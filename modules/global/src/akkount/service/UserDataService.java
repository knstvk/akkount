package akkount.service;

import com.haulmont.cuba.core.entity.Entity;

public interface UserDataService {
    String NAME = "akk_UserDataService";

    <T extends Entity> T loadEntity(String key, Class<T> entityClass);

    void saveEntity(String key, Entity entity);
}