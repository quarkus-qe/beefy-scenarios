package io.quarkus.qe.vertx.web.services;

import io.quarkus.qe.vertx.web.model.BladeRunner;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BladeRunnerService extends AbstractRedisDao<BladeRunner> {

    private static final String PREFIX = "bladeRunner_";

    public BladeRunnerService() {
        super(PREFIX, BladeRunner.class);
    }
}
