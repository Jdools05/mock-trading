package apis;

import database.daos.PasswordResetDao;
import database.daos.UserEntityDao;
import database.entities.PasswordResetEntity;
import database.entities.UserEntity;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import utilities.EmailHandler;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Tag(name = "Account Resource", description = "Endpoints for user account management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
@Path("/api/v1/account")
@ApplicationScoped
public class AccountResource {

    @Inject
    UserEntityDao userEntityDao;

    @Inject
    PasswordResetDao passwordResetDao;

    @Inject
    EmailHandler emailHandler;

    @POST
    @Path("/request-reset-password")
    public Response requestResetPassword(@QueryParam("email") String email) {
        UserEntity user = userEntityDao.findByEmail(email);
        if (user != null) {
            PasswordResetEntity passwordResetEntity = passwordResetDao.createPasswordResetToken(user);
            String token = passwordResetEntity.token;
            emailHandler.sendPasswordReset(user, token);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/reset-password")
    public Response resetPassword(@QueryParam("token") String token, @QueryParam("password") String password) {
        PasswordResetEntity passwordResetEntity = passwordResetDao.findByToken(token);
        if (passwordResetEntity != null) {
            if (passwordResetEntity.expirationDate.getTime() > System.currentTimeMillis()) {
                userEntityDao.updatePassword(passwordResetEntity.user.id, password);
                passwordResetDao.deletePasswordResetToken(passwordResetEntity.id);
                System.out.println("Password reset successful");
            }
        }
        return Response.ok().build();
    }
}
