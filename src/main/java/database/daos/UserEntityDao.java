package database.daos;

import database.entities.TransactionHistoryEntity;
import database.entities.UserEntity;
import io.quarkus.elytron.security.common.BcryptUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserEntityDao {

    @Transactional
    public UserEntity create(String firstName, String lastName, String email, String password, String role, double cash) {
        UserEntity entity = new UserEntity();
        entity.username = firstName + lastName;
        entity.firstName = firstName;
        entity.lastName = lastName;
        entity.email = email;
        entity.password = BcryptUtil.bcryptHash(password);
        entity.role = role;
        entity.cash = cash;
        entity.transactions = List.of();
        entity.persist();
        return entity;
    }

    @Transactional
    public UserEntity appendTransaction(UserEntity userEntity, TransactionHistoryEntity historyEntity) {
        userEntity.transactions.add(historyEntity);
        return userEntity;
    }

    public List<UserEntity> listAll() {
        return UserEntity.listAll();
    }

    public UserEntity get(int id) {
        return UserEntity.findById(id);
    }

    public UserEntity findByUsername(String username) {
        return UserEntity.find("username", username).firstResult();
    }
}
