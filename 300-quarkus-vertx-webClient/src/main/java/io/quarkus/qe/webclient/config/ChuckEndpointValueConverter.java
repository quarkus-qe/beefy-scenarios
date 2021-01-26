package io.quarkus.qe.vertx.webclient.config;

import org.eclipse.microprofile.config.spi.Converter;

public class ChuckEndpointValueConverter implements Converter<ChuckEndpointValue> {

    @Override
    public ChuckEndpointValue convert(String domain) {
        return new ChuckEndpointValue(domain + "/jokes/random");
    }
}
