/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.service;

import com.haulmont.cuba.core.entity.Entity;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface UserDataWorker {
    String NAME = "akk_UserDataWorker";

    <T extends Entity> T loadEntity(String key, Class<T> entityClass);

    void saveEntity(String key, Entity entity);
}
