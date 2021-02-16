package io.quarkus.qe.quartz;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/executions")
public class ExecutionsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExecutionEntity> get() {
        return ExecutionEntity.listAll();
    }
}
