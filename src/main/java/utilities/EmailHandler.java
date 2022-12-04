package utilities;

import database.entities.UserEntity;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class EmailHandler {

    @ConfigProperty(name="application.base-url")
    String baseUrl;

    @Inject
    Mailer mailer;

    public void sendEmail(String to, String subject, String body) {
        System.out.println("Sending email to " + to);
        mailer.send(Mail.withText(to, subject, body));
    }

    public void sendPasswordReset(UserEntity user, String token) {
        String subject = "Password Reset";
        String body = "Hello " + user.firstName + ",\n\n" +
                "You have requested a password reset. Please click the link below to reset your password.\n\n" +
                baseUrl + "/reset-password?token=" + token + "\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Regards,\n" +
                "Mock Trading Support";
        sendEmail(user.email, subject, body);
    }
}
