package apis;

import clients.models.finnhub.FinnhubQuote;
import clients.services.FinancialResourceClient;
import com.tietoevry.quarkus.resteasy.problem.HttpProblem;
import database.daos.StockEntityDao;
import database.daos.TransactionHistoryDao;
import database.daos.UserEntityDao;
import database.daos.WhitelistStockEntityDao;
import database.entities.StockEntity;
import database.entities.TransactionHistoryEntity;
import database.entities.UserEntity;
import enums.TradeType;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Tag(name = "Stock Resource", description = "Endpoints for stock data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"user", "admin"})
@Path("/api/v1/stocks")
@ApplicationScoped
public class StockResource {

    @Inject
    @RestClient
    FinancialResourceClient financialResourceClient;

    @Inject
    WhitelistStockEntityDao whitelistStockEntityDao;

    @Inject
    StockEntityDao stockEntityDao;

    @Inject
    UserEntityDao userEntityDao;

    @Inject
    TransactionHistoryDao transactionHistoryDao;

    @GET
    @Path("/quote")
    public FinnhubQuote quote(@Context SecurityContext context, @QueryParam("symbol") String symbol) throws ExecutionException, InterruptedException {
        FinnhubQuote quote = financialResourceClient.quote(symbol).toCompletableFuture().get();
        quote.symbol = symbol;
        return quote;
    }

    @POST
    @Path("/transaction")
    @Transactional
    public UserEntity appendTransaction(@Context SecurityContext securityContext, @NotNull @QueryParam("symbol") String symbol, @NotNull @QueryParam("amount") double amount, @NotNull @QueryParam("pricePu") double price, @NotNull @QueryParam("tradeType") TradeType tradeType) {
        if (price <= 0) {
            throw HttpProblem.builder()
                    .withStatus(Response.Status.fromStatusCode(422))
                    .withTitle("Invalid Price")
                    .withDetail("The price per unit must be greater than 0.")
                    .withInstance(URI.create("/api/v1/users/transaction"))
                    .build();
        }
        if (!whitelistStockEntityDao.isWhitelisted(symbol)) {
            throw HttpProblem.builder()
                    .withStatus(Response.Status.fromStatusCode(422))
                    .withTitle("Invalid Symbol")
                    .withDetail("The symbol " + symbol + " is not whitelisted.")
                    .withInstance(URI.create("/api/v1/users/transaction"))
                    .build();
        }
        if (amount <= 0) {
            throw HttpProblem.builder()
                    .withStatus(Response.Status.fromStatusCode(422))
                    .withTitle("Invalid Amount")
                    .withDetail("The amount must be greater than 0.")
                    .withInstance(URI.create("/api/v1/users/transaction"))
                    .build();
        }
        StockEntity stockEntity = stockEntityDao.create(symbol, amount);
        stockEntity.persist();
        UserEntity userEntity = userEntityDao.findByUsername(securityContext.getUserPrincipal().getName());
        List<StockEntity> userInv = userEntity.stocks;
        StockEntity requestedStock = userInv.stream().filter((s -> Objects.equals(s.symbol, symbol))).findFirst().orElse(null);
        if (tradeType == TradeType.BUY) {
            if (userEntity.cash < amount * price) {
                throw HttpProblem.builder()
                        .withStatus(Response.Status.fromStatusCode(422))
                        .withTitle("Insufficient Funds")
                        .withDetail("You do not have enough cash to complete this transaction.")
                        .withInstance(URI.create("/api/v1/users/transaction"))
                        .build();
            }
            if (requestedStock == null) {
                userInv.add(stockEntity);
                requestedStock = stockEntity;
            } else {
                requestedStock.amount += amount;
            }
            userEntity.cash -= amount * price;
        } else if (tradeType == TradeType.SELL) {
            if (requestedStock == null || requestedStock.amount < amount) {
                throw HttpProblem.builder()
                        .withStatus(Response.Status.fromStatusCode(422))
                        .withTitle("Insufficient Stock")
                        .withDetail("You do not have enough stock to complete this transaction.")
                        .withInstance(URI.create("/api/v1/users/transaction"))
                        .build();
            }
            requestedStock.amount -= amount;
            if (requestedStock.amount == 0) {
                userEntity.stocks.remove(requestedStock);
            }
            userEntity.cash += amount * price;
        }
        TransactionHistoryEntity transactionEntity = transactionHistoryDao.create(stockEntity, price, tradeType);
        userEntity = userEntityDao.appendTransaction(userEntity, transactionEntity);
        return userEntity;
    }
}
