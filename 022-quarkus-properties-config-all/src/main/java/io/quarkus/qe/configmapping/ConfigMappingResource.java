package io.quarkus.qe.configmapping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.smallrye.config.ConfigMapping;

@Path("/config-mapping")
public class ConfigMappingResource {
    @Inject
    Person personField;

    @Inject
    @ConfigMapping(prefix = "overrides.person")
    Person personOverridesField;

    @Inject
    PersonInterface personInterface;

    @GET
    @Path("/person/name/from-field")
    public String getPersonNameFromField() {
        return personField.name;
    }

    @GET
    @Path("/person/age/from-field")
    public int getPersonAgeFromField() {
        return personField.age;
    }

    @GET
    @Path("/person/name/from-overrides-field")
    public String getPersonNameFromOverridesField() {
        return personOverridesField.name;
    }

    @GET
    @Path("/person/age/from-overrides-field")
    public int getPersonAgeFromOverridesField() {
        return personOverridesField.age;
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
