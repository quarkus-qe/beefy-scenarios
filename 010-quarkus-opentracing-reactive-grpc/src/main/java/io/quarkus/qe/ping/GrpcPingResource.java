package io.quarkus.qe.ping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.example.PongRequest;
import io.quarkus.example.PongServiceGrpc;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.qe.traceable.TraceableResource;

@Path("/grpc-ping")
public class GrpcPingResource extends TraceableResource {

    @Inject
    @GrpcClient("pong")
    PongServiceGrpc.PongServiceBlockingStub pongClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPing() {
        recordTraceId();

        return "ping " + pongClient.sayPong(PongRequest.newBuilder().build()).getMessage();
    }
}
