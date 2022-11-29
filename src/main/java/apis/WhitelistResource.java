package apis;

import database.daos.StockRequestDao;
import database.daos.WhitelistStockEntityDao;
import database.entities.StockRequestEntity;
import database.entities.WhitelistStockEntity;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Tag(name = "Whitelist Resource", description = "Endpoints for whitelisted stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@Path("/api/v1/whitelist")
@ApplicationScoped
public class WhitelistResource {


    @Inject
    StockRequestDao stockRequestDao;

    @Inject
    WhitelistStockEntityDao whitelistStockEntityDao;

    @POST
    @Path("/request")
    @RolesAllowed({"user", "admin"})
    @Transactional
    public Response request(@QueryParam("symbol") String symbol, @QueryParam("name") String name, @QueryParam("exchange") String exchange) {
        stockRequestDao.addStockRequest(symbol, name, exchange);
        return Response.created(URI.create("/api/v1/whitelist/request")).build();
    }

    @GET
    @RolesAllowed({"user", "admin"})
    public List<WhitelistStockEntity> getWhitelist() {
        return whitelistStockEntityDao.getAll();
    }

    @GET
    @Path("/requests")
    public List<StockRequestEntity> getRequests() {
        return stockRequestDao.getAllStockRequests();
    }

    @PUT
    @Path("/approve/{id}")
    @Transactional
    public Response approve(@PathParam("id") Long id) {
        whitelistStockEntityDao.create(stockRequestDao.getStockRequest(id).stockSymbol);
        stockRequestDao.approveStockRequest(id);
        return Response.created(URI.create("/api/v1/whitelist/approve")).build();
    }

    @DELETE
    @Path("/request/{id}")
    @Transactional
    public Response deleteRequest(@PathParam("id") Long id) {
        String symbol = stockRequestDao.getStockRequest(id).stockSymbol;
        stockRequestDao.deleteStockRequests(symbol);
        return Response.ok().build();
    }
}
