package database.daos;

import database.entities.WhitelistStockEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Locale;

@ApplicationScoped
public class WhitelistStockDao {

        public boolean isWhitelisted(String symbol) {
            return WhitelistStockEntity.find("symbol", symbol.toUpperCase(Locale.ROOT)).firstResult() != null;
        }

        @Transactional
        public WhitelistStockEntity create(String symbol) {
            WhitelistStockEntity entity = new WhitelistStockEntity();
            entity.symbol = symbol.toUpperCase(Locale.ROOT);
            entity.persist();
            return entity;
        }
}
