package database.daos;

import database.entities.StockEntity;
import database.entities.StockRequestEntity;
import database.entities.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class StockRequestDao {

    @Transactional
    public StockRequestEntity create(String symbol, String reason, int userId) {
        StockRequestEntity entity = new StockRequestEntity();
        entity.symbol = symbol;
        entity.reason = reason;
        entity.user = UserEntity.findById(userId);
        entity.persist();
        return entity;
    }

    @Transactional
    public void delete(int id) {
        StockRequestEntity entity = StockRequestEntity.findById(id);
        entity.delete();
    }

    @Transactional
    public StockRequestEntity findBySymbol(String symbol) {
        return StockRequestEntity.find("symbol", symbol).firstResult();
    }

    @Transactional
    public void deleteAllBySymbol(String symbol) {
        StockRequestEntity.delete("symbol", symbol);
    }

    @Transactional
    public StockRequestEntity findById(int id) {
        return StockRequestEntity.findById(id);
    }

}
