package io.quarkus.qe.toggle;

public enum TogglableServices {
    SWAGGER("Swagger", "/q/swagger-ui", "quarkus.swagger-ui.enable"),
    OPENAPI("OpenAPI", "/q/openapi", "quarkus.smallrye-openapi.enable"),
    GRAPHQL("GraphQL", "/q/graphql-ui", "quarkus.smallrye-graphql.ui.enable"),
    HEALTH("Health", "/q/health-ui", "quarkus.smallrye-health.ui.enable");

    private final String name;
    private final String endpoint;
    private final String toggleProperty;

    TogglableServices(String name, String endpoint, String toggleProperty) {
        this.name = name;
        this.endpoint = endpoint;
        this.toggleProperty = toggleProperty;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getName() {
        return name;
    }

    public String getToggleProperty() {
        return toggleProperty;
    }
}
