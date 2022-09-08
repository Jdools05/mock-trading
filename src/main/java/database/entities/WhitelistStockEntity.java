package database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stock_table_whitelist")
public class WhitelistStockEntity extends PanacheEntity {

        public String symbol;

}
