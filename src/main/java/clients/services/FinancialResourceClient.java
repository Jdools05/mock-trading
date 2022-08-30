package clients.services;

import clients.authorizations.FinnhubAuthorizationFactory;
import clients.models.finnhub.FinnhubQuote;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.annotations.BaseUrl;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@RegisterRestClient(baseUri = "https://finnhub.io/api/v1")
@RegisterClientHeaders(FinnhubAuthorizationFactory.class)
@Singleton
public interface FinancialResourceClient {

    @Path("/quote")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<FinnhubQuote> quote(@QueryParam("symbol") String symbol);
}
