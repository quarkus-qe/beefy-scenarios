package io.quarkus.qe.vertx.web.services;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.qe.vertx.web.model.BladeRunner;

@ApplicationScoped
public class BladeRunnerService extends AbstractRedisDao<BladeRunner> {

    private static final String PREFIX = "bladeRunner_";

    public BladeRunnerService() {
        super(PREFIX, BladeRunner.class);
    }
}
