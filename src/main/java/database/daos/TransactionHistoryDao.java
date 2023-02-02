package database.daos;

import database.entities.StockEntity;
import database.entities.TransactionHistoryEntity;
import enums.TradeType;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class TransactionHistoryDao {

    @Transactional
    public TransactionHistoryEntity create(StockEntity stock, double price, TradeType tradeType) {
        TransactionHistoryEntity entity = new TransactionHistoryEntity();
        entity.timestamp = LocalDateTime.now();
        entity.stockSymbol = stock.symbol;
        entity.stockAmount = stock.amount;
        entity.price = price;
        entity.tradeType = tradeType;
        entity.persist();
        return entity;
    }
}
