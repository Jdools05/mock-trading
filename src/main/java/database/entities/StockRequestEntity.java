package database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class StockRequestEntity extends PanacheEntity {
    public String stockSymbol;
    public String stockName;
    public String stockExchange;
}
