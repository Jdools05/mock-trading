package database.daos;

import database.entities.WhitelistStockEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;

@ApplicationScoped
public class WhitelistStockEntityDao {

        public boolean isWhitelisted(String symbol) {
            return WhitelistStockEntity.find("symbol", symbol.toUpperCase(Locale.ROOT)).firstResult() != null;
        }
}
