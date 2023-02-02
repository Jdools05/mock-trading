package utilities;

import database.daos.PasswordResetDao;
import database.entities.PasswordResetEntity;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ScheduledMethods {

    @Inject
    PasswordResetDao passwordResetDao;

    @Scheduled(every = "5m")
    void CheckForExpiredPasswordResetTokens() {
        List<PasswordResetEntity> tokens = passwordResetDao.listAll();
        for (PasswordResetEntity token : tokens) {
            if (token.expirationDate.getTime() < System.currentTimeMillis()) {
                passwordResetDao.deletePasswordResetToken(token.id);
            }
        }
    }
}
