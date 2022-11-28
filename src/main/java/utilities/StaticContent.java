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
    @Route(path = "/home", methods = Route.HttpMethod.GET)
    void indexContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/index.html").handle(rc);
    }

    @Route(path = "/login", methods = Route.HttpMethod.GET)
    void loginContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/login.html").handle(rc);
    }

    @RolesAllowed("admin")
    @Route(path = "/admin", methods = Route.HttpMethod.GET)
    void adminContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/admin.html").handle(rc);
    }

//    @PermitAll
//    @Route(path = "/test", methods = Route.HttpMethod.GET)
//    void testContent(RoutingContext rc) {
//        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/test.html").handle(rc);
//    }

    @PermitAll
    @Route(path = "/darkmode.css", methods = Route.HttpMethod.GET)
    void darkmodeContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "META-INF/bootstrap.min.css").handle(rc);
    }

    @PermitAll
    @Route(path = "/favicon.ico", methods = Route.HttpMethod.GET)
    void faviconContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "META-INF/favicon.ico").handle(rc);
    }

    @Route(path = "/register", methods = Route.HttpMethod.GET)
    void registerContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/register.html").handle(rc);
    }
}
