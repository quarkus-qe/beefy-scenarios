package io.quarkus.qe.configmapping;

import io.smallrye.config.ConfigMapping;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/config-mapping")
public class ConfigMappingResource {
    @Inject
    @ConfigMapping(prefix = "overrides.person")
    PersonInterface personOverridesInterface;

    @Inject
    PersonInterface personInterface;

    @GET
    @Path("/person/name/from-overrides-interface")
    public String getPersonNameFromOverridesInterface() {
        return personOverridesInterface.name();
    }

    @GET
    @Path("/person/age/from-overrides-interface")
    public int getPersonAgeFromOverridesInterface() {
        return personOverridesInterface.age();
    }

    @GET
    @Path("/person/name/from-interface")
    public String getPersonNameFromInterface() {
        return personInterface.name();
    }

    @GET
    @Path("/person/age/from-interface")
    public int getPersonAgeFromInterface() {
        return personInterface.age();
    }

    @GET
    @Path("/person/label/{labelKey}/from-interface")
    public String getPersonLabelFromInterface(@PathParam("labelKey") String labelKey) {
        return personInterface.labels().get(labelKey);
    }
}
