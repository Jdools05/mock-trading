package apis;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Tag(name = "Whitelist Resource", description = "Endpoints for whitelisted stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"user", "admin"})
@Path("/api/v1/whitelist")
@ApplicationScoped
public class WhitelistResource {

    @POST
    @Path("/request")
    @Transactional
    public Response request() {
        // TODO: Add the requested stock to the whitelist queue.
        return Response.created(URI.create("/api/v1/whitelist/request")).build();
    }
}
