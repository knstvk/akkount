package akkount.service;

import com.haulmont.cuba.core.entity.Entity;

import java.util.List;

public interface UserDataService {
    String NAME = "akk_UserDataService";

    <T extends Entity> T loadEntity(String key, Class<T> entityClass);

    <T extends Entity> List<T> loadEntityList(String key, Class<T> entityClass);

    void saveEntity(String key, Entity entity);

    void addEntity(String key, Entity entity);

    void removeEntity(String key, Entity entity);
}