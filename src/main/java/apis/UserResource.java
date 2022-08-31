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
import java.util.concurrent.ExecutionException;

@Tag(name = "User Resource", description = "Endpoints for user data")
@Path("/users")
@RolesAllowed({"user", "admin"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserEntity> listAll() {
        return userEntityDao.listAll();
    }

    @GET
    @Path("/quote")
    public FinnhubQuote quote(@QueryParam("symbol") String symbol) throws ExecutionException, InterruptedException {
        FinnhubQuote quote = financialResourceClient.quote(symbol).toCompletableFuture().get();
        quote.symbol = symbol;
        return quote;
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PermitAll
    public UserEntity createUser(@NotNull @QueryParam("firstName") String firstName, @NotNull @QueryParam("lastName") String lastName, @NotNull @QueryParam("password") String password, @NotNull @QueryParam("email") String email, @NotNull @QueryParam("role") String role, @NotNull @QueryParam("cash") double cash) {
        if (userEntityDao.findByUsername(firstName + lastName) != null) {
            throw new WebApplicationException(Response.status(400).build());
        }
        UserEntity entity = userEntityDao.create(firstName, lastName, email, password, role, cash);
        return entity;
    }

    @GET
    @Path("/apikey")
    @Produces(MediaType.APPLICATION_JSON)
    public String apikey() {
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
        if (tradeType == TradeType.BUY && userEntity.cash < amount * price) {
            throw new WebApplicationException(Response.status(400).build());
        } else if (tradeType == TradeType.SELL) {

        }
        TransactionHistoryEntity transactionEntity = transactionHistoryDao.create(stockEntity, price, tradeType);
        userEntity = userEntityDao.appendTransaction(userEntity, transactionEntity);
        return userEntity;
    }
}
