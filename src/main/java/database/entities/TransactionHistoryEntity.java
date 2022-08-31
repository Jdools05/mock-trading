package database.entities;

import enums.TradeType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history_entity_table")
public class TransactionHistoryEntity extends PanacheEntity {

    @Column(name = "timestamp")
    public LocalDateTime timestamp;

    @JoinColumn(name = "stock")
    @OneToOne
    public StockEntity stock;

    @Column(name = "price")
    public double price;

    @Column(name = "trade_type")
    public TradeType tradeType;

}
