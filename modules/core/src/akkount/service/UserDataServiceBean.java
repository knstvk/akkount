package akkount.service;

import com.haulmont.cuba.core.entity.Entity;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(UserDataService.NAME)
public class UserDataServiceBean implements UserDataService {

    @Inject
    protected UserDataWorker worker;

    @Override
    public <T extends Entity> T loadEntity(String key, Class<T> entityClass) {
        return worker.loadEntity(key, entityClass);
    }

    @Override
    public void saveEntity(String key, Entity entity) {
        worker.saveEntity(key, entity);
    }
}