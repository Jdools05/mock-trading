package utilities;

import io.quarkus.vertx.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StaticContent {

    @RolesAllowed({"user", "admin"})
    @Route(path = "/app/home", methods = Route.HttpMethod.GET)
    void indexContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/index.html").handle(rc);
    }

    @Route(path = "/app/login", methods = Route.HttpMethod.GET)
    void loginContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/login.html").handle(rc);
    }

    @Route(path = "/app/whitelist", methods = Route.HttpMethod.GET)
    void whitelistContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/whitelist.html").handle(rc);
    }

    @RolesAllowed("admin")
    @Route(path = "/app/admin", methods = Route.HttpMethod.GET)
    void adminContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/admin.html").handle(rc);
    }

    @PermitAll
    @Route(path = "/app/reset-password", methods = Route.HttpMethod.GET)
    void resetPasswordContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/reset-password.html").handle(rc);
    }

    @PermitAll
    @Route(path = "/app/request-password-reset", methods = Route.HttpMethod.GET)
    void requestPasswordResetContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/request-password-reset.html").handle(rc);
    }

    @RolesAllowed({"user", "admin"})
    @Route(path = "/app/instructions", methods = Route.HttpMethod.GET)
    void instructionsContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/instructions.html").handle(rc);
    }



//    @PermitAll
//    @Route(path = "/app/test", methods = Route.HttpMethod.GET)
//    void testContent(RoutingContext rc) {
//        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/test.html").handle(rc);
//    }

    @PermitAll
    @Route(path = "/app/darkmode.css", methods = Route.HttpMethod.GET)
    void darkmodeContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "META-INF/bootstrap.min.css").handle(rc);
    }

    @PermitAll
    @Route(path = "/app/favicon.ico", methods = Route.HttpMethod.GET)
    void faviconContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "META-INF/favicon.ico").handle(rc);
    }

    @Route(path = "/app/register", methods = Route.HttpMethod.GET)
    void registerContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/register.html").handle(rc);
    }
}
