package utilities;

import database.entities.UserEntity;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class EmailHandler {

    @ConfigProperty(name="application.base-url")
    String baseUrl;

    @Inject
    ReactiveMailer mailer;

    public Uni<Void> sendEmail(String to, String subject, String body) {
        System.out.println("Sending email to " + to);
        return mailer.send(Mail.withText(to, subject, body));
    }

    public Uni<Void> sendPasswordReset(UserEntity user, String token) {
        String subject = "Password Reset";
        String body = "Hello " + user.firstName + ",\n\n" +
                "You have requested a password reset. Please click the link below to reset your password.\n\n" +
                baseUrl + "/reset-password?token=" + token + "\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Regards,\n" +
                "Mock Trading Support";
        Uni<Void> email = sendEmail(user.email, subject, body);
        email.subscribe().with(
                x -> System.out.println("Email sent to " + user.email),
                x -> System.out.println("Email failed to send to " + user.email)
        );
        return email;
    }
}
