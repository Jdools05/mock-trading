package database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stock_request_table")
public class StockRequestEntity extends PanacheEntity {
    @Column(name = "stock_symbol", nullable = false, unique = true)
    public String stockSymbol;

    @Column(name = "stock_name", nullable = false)
    public String stockName;

    @Column(name = "stock_exchange", nullable = false)
    public String stockExchange;
}
