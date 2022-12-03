package apis;

import database.daos.PasswordResetDao;
import database.daos.UserEntityDao;
import database.entities.PasswordResetEntity;
import database.entities.UserEntity;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.annotations.Pos;
import utilities.EmailHandler;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Tag(name = "Account Resource", description = "Endpoints for user account management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
@Path("/api/v1/stocks")
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
    public void requestResetPassword(@QueryParam("email") String email) {
        UserEntity user = userEntityDao.findByEmail(email);
        if (user != null) {
            PasswordResetEntity passwordResetEntity = passwordResetDao.createPasswordResetToken(user);
            String token = passwordResetEntity.token;
            emailHandler.sendPasswordReset(user, token);
        }
    }

    @PUT
    @Path("/reset-password")
    public void resetPassword(@QueryParam("token") String token, @QueryParam("password") String password) {
        PasswordResetEntity passwordResetEntity = passwordResetDao.findByToken(token);
        if (passwordResetEntity != null) {
            if (passwordResetEntity.expirationDate.getTime() > System.currentTimeMillis()) {
                userEntityDao.updatePassword(passwordResetEntity.user.id, password);
                passwordResetDao.deletePasswordResetToken(passwordResetEntity.id);
            }
        }
    }
}
