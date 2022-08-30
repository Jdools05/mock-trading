package clients.authorizations;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

@ApplicationScoped
public class FinnhubAuthorizationFactory implements ClientHeadersFactory {

    @ConfigProperty(name = "ApiKeys.finnhubApiKey")
    String finnhubApiKey;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        MultivaluedMap<String, String> result = new MultivaluedMapImpl<>();

        result.add("X-Finnhub-Token", finnhubApiKey);

        return result;
    }
}
