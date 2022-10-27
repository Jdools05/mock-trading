package apis;

import database.daos.StockRequestDao;
import database.daos.UserEntityDao;
import database.daos.WhitelistStockDao;
import database.entities.StockRequestEntity;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Tag(name = "Stock Resource", description = "Endpoints for stock data")
@Path("/api/v1/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"user", "admin"})
public class StockResource {

    @Inject
    UserEntityDao userEntityDao;

    @Inject
    StockRequestDao stockRequestDao;

    @Inject
    WhitelistStockDao whitelistStockDao;

    @GET
    @Path("/requests/list")
    @Transactional
    public List<StockRequestEntity> listAll() {
        return StockRequestEntity.listAll();
    }

    @POST
    @Path("/request")
    @Transactional
    public StockRequestEntity requestStock(@QueryParam("symbol") String symbol, @QueryParam("reason") String reason, @QueryParam("username") String username) {
        StockRequestEntity entity = new StockRequestEntity();
        entity.symbol = symbol;
        entity.reason = reason;
        entity.user = userEntityDao.findByUsername(username);
        entity.persist();
        return entity;
    }

    @DELETE
    @Path("/request")
    @RolesAllowed("admin")
    @Transactional
    public Response deleteStockRequest(@QueryParam("id") int id, @QueryParam("symbol") String symbol) {
        if (id == 0) {
            stockRequestDao.deleteAllBySymbol(symbol);
        } else {
            stockRequestDao.delete(id);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/request/approve")
    @RolesAllowed("admin")
    @Transactional
    public Response approveStockRequest(@QueryParam("id") int id, @QueryParam("userId") String symbol) {
        StockRequestEntity stock;
        if (id == 0) {
            stock = stockRequestDao.findBySymbol(symbol);

        } else {
            stock = stockRequestDao.findById(id);
        }
        if (stock != null && !whitelistStockDao.isWhitelisted(symbol)) {
            whitelistStockDao.create(stock.symbol);
        }
        stockRequestDao.deleteAllBySymbol(symbol);
        return Response.ok().build();
    }

}
