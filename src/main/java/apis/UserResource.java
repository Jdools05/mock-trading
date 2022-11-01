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
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
    WhitelistStockEntityDao whitelistStockEntityDao;

    @GET
    @Path("/list")
    @RolesAllowed("admin")
    public List<UserEntity> listAll(@Context SecurityContext context) {
        return userEntityDao.listAll();
    }

    @GET
    @Path("/me")
    public UserEntity listByUsername(@Context SecurityContext context) {
        return userEntityDao.findByUsername(context.getUserPrincipal().getName());
    }

    @GET
    @Path("/{username}")
    @RolesAllowed("admin")
    public UserEntity listByUsername(@PathParam("username") String username) {
        return userEntityDao.findByUsername(username);
    }

    @GET
    @Path("/username-available")
    @PermitAll
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
    @Path("email-available")
    @PermitAll
    public boolean isEmailAvailable(@Context SecurityContext context, @QueryParam("email") String email) {
        return userEntityDao.findByEmail(email) == null;
    }


}
