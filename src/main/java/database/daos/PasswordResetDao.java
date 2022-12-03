package database.daos;

import com.mchange.util.Base64Encoder;
import database.entities.PasswordResetEntity;
import database.entities.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class PasswordResetDao {

    public static final SecureRandom secureRandom = new SecureRandom();
    public static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Transactional
    public PasswordResetEntity createPasswordResetToken(UserEntity user) {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        String token = base64Encoder.encodeToString(bytes);
        PasswordResetEntity passwordResetEntity = new PasswordResetEntity();
        passwordResetEntity.token = token;
        passwordResetEntity.expirationDate = new Timestamp(System.currentTimeMillis() + 3600000);
        passwordResetEntity.user = user;
        passwordResetEntity.persist();
        return passwordResetEntity;
    }

    @Transactional
    public PasswordResetEntity findByToken(String token) {
        return PasswordResetEntity.find("token", token).firstResult();
    }

    @Transactional
    public void deletePasswordResetToken(String token) {
        PasswordResetEntity passwordResetEntity = PasswordResetEntity.find("token", token).firstResult();
        passwordResetEntity.delete();
    }

    @Transactional
    public void deletePasswordResetToken(long id) {
        PasswordResetEntity passwordResetEntity = PasswordResetEntity.findById(id);
        passwordResetEntity.delete();
    }

    @Transactional
    public List<PasswordResetEntity> listAll() {
        return PasswordResetEntity.listAll();
    }
}
