package io.quarkus.qe.vertx.web.services;

import io.quarkus.qe.vertx.web.model.Replicant;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplicantService extends AbstractRedisDao<Replicant> {

    private static final String PREFIX = "replicant_";

    public ReplicantService() {
        super(PREFIX, Replicant.class);
    }
}
