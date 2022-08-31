package database.daos;

import database.entities.StockEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class StockEntityDao {

    @Transactional
    public StockEntity create(String symbol, double amount) {
        StockEntity entity = new StockEntity();
        entity.symbol = symbol;
        entity.amount = amount;
        return entity;
    }
}
