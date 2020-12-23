package io.quarkus.qe.vertx.web.services;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.qe.vertx.web.model.Replicant;

@ApplicationScoped
public class ReplicantService extends AbstractRedisDao<Replicant> {

    private static final String PREFIX = "replicant_";

    public ReplicantService() {
        super(PREFIX, Replicant.class);
    }
}
