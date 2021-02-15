package io.quarkus.qe.quartz.resources;

import java.util.Collections;

import io.quarkus.qe.quartz.ExecutionEntity;
import io.quarkus.qe.quartz.ExecutionsResource;

public class RestApplicationResource extends ApplicationResource {

    public static final String QUARKUS_HTTP_PORT = "quarkus.http.port";

    public RestApplicationResource(int port) {
        super(classes(ExecutionsResource.class, ExecutionEntity.class), Collections.singletonMap(QUARKUS_HTTP_PORT, "" + port));
    }

}
