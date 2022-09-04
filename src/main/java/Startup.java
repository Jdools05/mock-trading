import database.daos.UserEntityDao;
import database.entities.UserEntity;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    UserEntityDao userEntityDao;

    @Transactional
    public void addUsers(@Observes StartupEvent event) {
        userEntityDao.create("admin", "user", "test@gmail.com", "password", "admin", 999999);
    }
}
