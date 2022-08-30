package database.daos;

import database.entities.TransactionHistoryEntity;
import enums.TradeType;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class TransactionHistoryDao {

    @Transactional
    public TransactionHistoryEntity create(String symbol, double amount, double price, TradeType tradeType) {
        TransactionHistoryEntity entity = new TransactionHistoryEntity();
        entity.timestamp = LocalDateTime.now();
        entity.symbol = symbol;
        entity.amount = amount;
        entity.price = price;
        entity.tradeType = tradeType;
        entity.persist();
        return entity;
    }
}
