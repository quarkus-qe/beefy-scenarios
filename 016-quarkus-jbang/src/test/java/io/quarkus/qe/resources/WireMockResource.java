package io.quarkus.qe.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class WireMockResource implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;
    private int port;
    private String host;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        return Collections.singletonMap("api.path", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (Objects.nonNull(wireMockServer)) wireMockServer.stop();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEndpoint() {
        return "http://" + Optional.ofNullable(host).orElse("127.0.0.1") + ":" + Optional.ofNullable(port).orElse(8080);
    }
}