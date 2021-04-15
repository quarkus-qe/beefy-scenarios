package io.quarkus.qe.bulk;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "server")
public class BulkOfPropertiesConfiguration {
    String url;
    String host;
    String path;
    int port;

    @ConfigProperty(name = "port")
    int repeatedPort;

    @ConfigProperty(name = "url.with.default.found")
    String urlWithDefaultAndConfigFound;

    @ConfigProperty(name = "url.with.default.not.found")
    String urlWithDefaultAndConfigNotFound;

    @ConfigProperty(name = "url.with.default.nested")
    String urlWithDefaultNested;

    @ConfigProperty(name = "url.composed")
    String urlComposed;

    @ConfigProperty(name = "url.raw")
    String urlRaw;
}
