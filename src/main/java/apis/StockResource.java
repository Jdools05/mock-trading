package apis;

import clients.models.finnhub.FinnhubQuote;
import clients.services.FinancialResourceClient;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.concurrent.ExecutionException;

@Tag(name = "Stock Resource", description = "Endpoints for stock data")
@Path("/api/v1/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"user", "admin"})
public class StockResource {

    @Inject
    @RestClient
    FinancialResourceClient financialResourceClient;

    @GET
    public FinnhubQuote quote(@Context SecurityContext context, @QueryParam("symbol") String symbol) throws ExecutionException, InterruptedException {
        FinnhubQuote quote = financialResourceClient.quote(symbol).toCompletableFuture().get();
        quote.symbol = symbol;
        return quote;
    }
}
