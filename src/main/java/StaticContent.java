import io.quarkus.vertx.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
public class StaticContent {

    @Route(path = "/home", methods = Route.HttpMethod.GET)
    void indexContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/index.html").handle(rc);
    }

    @Route(path = "/login", methods = Route.HttpMethod.GET)
    void loginContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/login.html").handle(rc);
    }

    @Route(path = "/admin", methods = Route.HttpMethod.GET)
    void adminContent(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "frontend/admin.html").handle(rc);
    }
}
