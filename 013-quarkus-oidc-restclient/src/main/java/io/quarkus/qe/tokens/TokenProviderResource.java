package io.quarkus.qe.tokens;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClients;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/generate-token")
public class TokenProviderResource {
    @Inject
    OidcClient oidcClient;

    @Inject
    OidcClients allOidcClients;

    @GET
    @Path("/client-credentials")
    public String getTokenUsingClientCredentialsGrant() {
        return generateToken(oidcClient);
    }

    @GET
    @Path("/test-user")
    public String getTokenUsingAdminUserPasswordGrant() {
        return generateToken(allOidcClients.getClient("test-user"));
    }

    private String generateToken(OidcClient client) {
        return client.getTokens().await().indefinitely().getAccessToken();
    }
}
