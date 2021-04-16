package io.quarkus.qe.vertx.webclient.resources;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockChuckNorrisResource implements QuarkusTestResourceLifecycleManager {
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        return Collections.singletonMap("chucknorris.api.domain", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (Objects.nonNull(wireMockServer))
            wireMockServer.stop();
    }
}
