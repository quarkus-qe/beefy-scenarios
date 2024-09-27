package io.quarkus.qe.ping.clients;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import io.quarkus.qe.model.Score;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
@ClientHeaderParam(name = "Authorization", value = "{lookupAuth}")
@Path("/rest-pong")
public interface LookupAuthorizationPongClient {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String getPong();

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    String getPongWithPathName(@PathParam("name") String name);

    @POST
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    String createPongWithBody(Score score);

    @PUT
    @Path("/withBody")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    String updatePongWithBody(Score score);

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    boolean deletePongById(@PathParam("id") String id);

    default String lookupAuth() {
        Config config = ConfigProvider.getConfig();

        String oidcAuthUrl = config.getValue("quarkus.oidc.auth-server-url", String.class);
        String realm = oidcAuthUrl.substring(oidcAuthUrl.lastIndexOf("/") + 1);
        String authUrl = oidcAuthUrl.replace("/realms/" + realm, "");
        String clientId = config.getValue("quarkus.oidc.client-id", String.class);
        String clientSecret = config.getValue("quarkus.oidc.credentials.secret", String.class);

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("password")
                .username("alice")
                .password("alice")
                .build();

        String keycloakToken = keycloak.tokenManager().getAccessToken().getToken();
        return "Bearer " + keycloakToken;
    }
}
