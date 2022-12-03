package database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "password_reset_table")
public class PasswordResetEntity extends PanacheEntity {
    @Column(name = "token", nullable = false, unique = true)
    public String token;

    @Column(name = "expiration_date", nullable = false)
    public Timestamp expirationDate;

    @OneToOne
    public UserEntity user;
}
