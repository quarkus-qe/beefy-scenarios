package io.quarkus.qe.bulk;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/config-value")
public class ConfigValueResource {

    @ConfigProperty(name = "server.url")
    ConfigValue serverUrl;

    @GET
    @Path("/serverUrl/name")
    public String getServerUrlName() {
        return serverUrl.getName();
    }

    @GET
    @Path("/serverUrl/sourceName")
    public String getServerUrlSourceName() {
        return serverUrl.getSourceName();
    }

    @GET
    @Path("/serverUrl/value")
    public String getServerUrlValue() {
        return serverUrl.getValue();
    }

    @GET
    @Path("/serverUrl/rawValue")
    public String getServerUrlRawValue() {
        return serverUrl.getRawValue();
    }
}
