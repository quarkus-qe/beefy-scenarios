package io.quarkus.qe.quartz;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/executions")
public class ExecutionsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExecutionEntity> get() {
        return ExecutionEntity.listAll();
    }
}
