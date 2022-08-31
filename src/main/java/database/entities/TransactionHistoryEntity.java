package database.entities;

import enums.TradeType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaction_history_entity_table")
public class TransactionHistoryEntity extends PanacheEntity {

    @Column(name = "timestamp")
    public LocalDateTime timestamp;

    @Column(name = "stock")
    public StockEntity stock;

    @Column(name = "price")
    public double price;

    @Column(name = "trade_type")
    public TradeType tradeType;

}
