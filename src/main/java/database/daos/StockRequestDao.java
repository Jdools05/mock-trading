package database.daos;

import database.entities.StockRequestEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StockRequestDao {

    @Transactional
    public void addStockRequest(String stockSymbol, String stockName, String stockExchange) {
        StockRequestEntity stockRequestEntity = new StockRequestEntity();
        stockRequestEntity.stockSymbol = stockSymbol;
        stockRequestEntity.stockName = stockName;
        stockRequestEntity.stockExchange = stockExchange;
        stockRequestEntity.persist();
    }

    @Transactional
    public void approveStockRequest(Long id) {
        StockRequestEntity stockRequestEntity = StockRequestEntity.findById(id);
        String stockSymbol = stockRequestEntity.stockSymbol;
        deleteStockRequests(stockSymbol);
    }

    public StockRequestEntity getStockRequest(String stockSymbol) {
        return StockRequestEntity.find("stockSymbol", stockSymbol).firstResult();
    }

    public List<StockRequestEntity> getAllStockRequests() {
        return StockRequestEntity.listAll();
    }

    @Transactional
    public void deleteStockRequests(String stockSymbol) {
        StockRequestEntity.delete("stockSymbol", stockSymbol);
    }

    @Transactional
    public void deleteStockRequest(long id) {
        StockRequestEntity stockRequestEntity = StockRequestEntity.findById(id);
        stockRequestEntity.delete();
    }

    @Transactional
    public StockRequestEntity getStockRequest(long id) {
        return StockRequestEntity.findById(id);
    }
}
