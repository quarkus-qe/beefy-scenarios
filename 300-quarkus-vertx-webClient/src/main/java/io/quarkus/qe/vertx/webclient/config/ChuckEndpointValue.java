package io.quarkus.qe.vertx.webclient.config;

public class ChuckEndpointValue {

    private final String value;

    public ChuckEndpointValue(String endpoint) {
        this.value = endpoint;
    }

    public String getValue() {
        return value;
    }
}
