package apis;

import clients.models.finnhub.FinnhubQuote;
import clients.services.FinancialResourceClient;
import database.daos.StockEntityDao;
import database.daos.TransactionHistoryDao;
import database.daos.UserEntityDao;
import database.entities.StockEntity;
import database.entities.TransactionHistoryEntity;
import database.entities.UserEntity;
import enums.TradeType;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Tag(name = "User Resource", description = "Endpoints for user data")
@Path("/api/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"user", "admin"})
public class UserResource {

    @Inject
    @RestClient
    FinancialResourceClient financialResourceClient;


    @Inject
    UserEntityDao userEntityDao;

    @Inject
    TransactionHistoryDao transactionHistoryDao;

    @Inject
    StockEntityDao stockEntityDao;

    @GET
    @Path("/list")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserEntity> listAll(@Context SecurityContext context) {
        return userEntityDao.listAll();
    }

    @GET
    @Path("/quote")
    public FinnhubQuote quote(@Context SecurityContext context, @QueryParam("symbol") String symbol) throws ExecutionException, InterruptedException {
        FinnhubQuote quote = financialResourceClient.quote(symbol).toCompletableFuture().get();
        quote.symbol = symbol;
        return quote;
    }

    @GET
    @Path("/username-available")
    @PermitAll
    public boolean isUsernameAvailable(@Context SecurityContext context, @QueryParam("username") String username) {
        return userEntityDao.findByUsername(username) == null;
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PermitAll
    public UserEntity createUser(@NotNull @QueryParam("username") String username, @NotNull @QueryParam("firstName") String firstName, @NotNull @QueryParam("lastName") String lastName, @NotNull @QueryParam("password") String password, @NotNull @QueryParam("email") String email) {
        System.out.println("Creating user: " + username);
        if (userEntityDao.findByUsername(username) != null || userEntityDao.findByEmail(email) != null) {
            throw new WebApplicationException(Response.status(400).build());
        }
        UserEntity entity = userEntityDao.create(username, firstName, lastName, email, password);
        return entity;
    }

    @GET
    @Path("/apikey")
    @Produces(MediaType.APPLICATION_JSON)
    public String apikey(@Context SecurityContext context) {
        return ConfigProvider.getConfig().getValue("ApiKeys.finnhubApiKey", String.class);
    }

    @POST
    @Path("/transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserEntity appendTransaction(@Context SecurityContext securityContext, @NotNull @QueryParam("symbol") String symbol, @NotNull @QueryParam("amount") double amount, @NotNull @QueryParam("pricePU") double price, @NotNull @QueryParam("tradeType") TradeType tradeType) {
        StockEntity stockEntity = stockEntityDao.create(symbol, amount);
        UserEntity userEntity = userEntityDao.findByUsername(securityContext.getUserPrincipal().getName());
        List<StockEntity> userInv = userEntity.stocks;
        StockEntity requestedStock = userInv.stream().filter((s -> Objects.equals(s.symbol, symbol))).collect(Collectors.toList()).get(0);
        if (tradeType == TradeType.BUY) {
            if (userEntity.cash < amount * price) {
                throw new WebApplicationException(Response.status(400).build());
            }
            if (requestedStock == null) {
                userInv.add(stockEntity);
                requestedStock = stockEntity;
            }
        } else if (tradeType == TradeType.SELL) {
            if (requestedStock != null || requestedStock.amount < amount) {
                throw new WebApplicationException(Response.status(400).build());
            }
            requestedStock.amount -= amount;
            if (requestedStock.amount == 0) {
                userEntity.stocks.remove(requestedStock);
            }
        }
        TransactionHistoryEntity transactionEntity = transactionHistoryDao.create(stockEntity, price, tradeType);
        userEntity = userEntityDao.appendTransaction(userEntity, transactionEntity);
        return userEntity;
    }
}
