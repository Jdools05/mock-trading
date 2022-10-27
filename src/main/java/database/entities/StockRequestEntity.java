package database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stock_request_table")
public class StockRequestEntity extends PanacheEntity {
    public String symbol;
    public String reason;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
