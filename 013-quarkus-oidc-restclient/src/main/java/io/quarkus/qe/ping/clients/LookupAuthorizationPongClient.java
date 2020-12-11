package io.quarkus.qe.ping.clients;

import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.impl.client.HttpClients;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;

@RegisterRestClient
@ClientHeaderParam(name = "Authorization", value = "{lookupAuth}")
public interface LookupAuthorizationPongClient {
    @GET
    @Path("/rest-pong")
    @Produces(MediaType.TEXT_PLAIN)
    String getPong();

    default String lookupAuth() {
        Config config = ConfigProvider.getConfig();

        String oidcAuthUrl = config.getValue("quarkus.oidc.auth-server-url", String.class);
        String realm = oidcAuthUrl.substring(oidcAuthUrl.lastIndexOf("/") + 1);
        String authUrl = oidcAuthUrl.replace("/realms/" + realm, "");
        String clientId = config.getValue("quarkus.oidc.client-id", String.class);
        String clientSecret = config.getValue("quarkus.oidc.credentials.secret", String.class);

        AuthzClient authzClient = AuthzClient.create(new Configuration(
                authUrl,
                realm,
                clientId,
                Collections.singletonMap("secret", clientSecret),
                HttpClients.createDefault()));

        return "Bearer " + authzClient.obtainAccessToken("test-user", "test-user").getToken();
    }
}
