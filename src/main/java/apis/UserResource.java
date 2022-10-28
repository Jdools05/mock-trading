package apis;

import clients.models.finnhub.FinnhubQuote;
import clients.services.FinancialResourceClient;
import com.tietoevry.quarkus.resteasy.problem.HttpProblem;
import database.daos.StockEntityDao;
import database.daos.TransactionHistoryDao;
import database.daos.UserEntityDao;
import database.daos.WhitelistStockDao;
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
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

    @Inject
    WhitelistStockDao whitelistStockDao;

    @GET
    @Path("/list")
    @RolesAllowed("admin")
<<<<<<< Updated upstream
=======
    @Produces(MediaType.APPLICATION_JSON)
>>>>>>> Stashed changes
    @Transactional
    public List<UserEntity> listAll(@Context SecurityContext context) {
        return userEntityDao.listAll();
    }

    @GET
    @Path("/me")
    @Transactional
    public UserEntity listByUsername(@Context SecurityContext context) {
        return userEntityDao.findByUsername(context.getUserPrincipal().getName());
    }

    @GET
    @Path("/{username}")
    @RolesAllowed("admin")
    @Transactional
    public UserEntity listByUsername(@PathParam("username") String username) {
        return userEntityDao.findByUsername(username);
    }

    @GET
    @Path("/quote")
    @Transactional
    public FinnhubQuote quote(@Context SecurityContext context, @QueryParam("symbol") String symbol) throws ExecutionException, InterruptedException {
        FinnhubQuote quote = financialResourceClient.quote(symbol).toCompletableFuture().get();
        quote.symbol = symbol;
        return quote;
    }

    @GET
    @Path("/username-available")
    @PermitAll
    @Transactional
    public boolean isUsernameAvailable(@Context SecurityContext context, @QueryParam("username") String username) {
        return userEntityDao.findByUsername(username) == null;
    }

    @POST
    @Path("/create")
    @Transactional
    @PermitAll
    public UserEntity createUser(@NotNull @QueryParam("username") String username, @NotNull @QueryParam("firstName") String firstName, @NotNull @QueryParam("lastName") String lastName, @NotNull @QueryParam("password") String password, @NotNull @QueryParam("email") String email) {
        System.out.println("Creating user: " + username);
        if (userEntityDao.findByUsername(username) != null) {
            throw HttpProblem.builder()
                    .withStatus(Response.Status.fromStatusCode(422))
                    .withTitle("User Already Exists")
                    .withDetail("A user with the username of " + username + " already exists in the system.")
                    .withInstance(URI.create("/api/v1/users/create"))
                    .build();
        }
        if (userEntityDao.findByEmail(email) != null) {
            throw HttpProblem.builder()
                    .withStatus(Response.Status.fromStatusCode(422))
                    .withTitle("User Already Exists")
                    .withDetail("A user with the email of " + email + " already exists in the system.")
                    .withInstance(URI.create("/api/v1/users/create"))
                    .build();
        }
        UserEntity entity = userEntityDao.create(username, firstName, lastName, email, password);
        return entity;
    }

    @GET
<<<<<<< Updated upstream
=======
    @Path("/apikey")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String apikey(@Context SecurityContext context) {
        return ConfigProvider.getConfig().getValue("ApiKeys.finnhubApiKey", String.class);
    }

    @GET
>>>>>>> Stashed changes
    @Path("email-available")
    @PermitAll
    @Transactional
    public boolean isEmailAvailable(@Context SecurityContext context, @QueryParam("email") String email) {
        return userEntityDao.findByEmail(email) == null;
    }

    @PUT
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
        if (!whitelistStockDao.isWhitelisted(symbol)) {
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
