package database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Stock extends PanacheEntity {

    public String symbol;
    public double amount;

}
