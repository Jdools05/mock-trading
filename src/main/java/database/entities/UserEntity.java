package database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.User;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@UserDefinition
@Table(name = "user_table")
@Entity
public class UserEntity extends PanacheEntity {

    @Password
    @JsonIgnore
    public String password;

    @Email
    @Username
    public String email;

    @Roles
    public String role;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "cash")
    public double cash;

    @Column(name = "stocks")
    @OneToMany
    public List<StockEntity> stocks;

    @Column(name = "transaction_history")
    @OneToMany
    public List<TransactionHistoryEntity> transactions;
}
