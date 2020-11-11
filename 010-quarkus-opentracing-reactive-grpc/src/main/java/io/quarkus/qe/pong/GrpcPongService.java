package io.quarkus.qe.pong;

import javax.inject.Singleton;

import org.jboss.logmanager.MDC;

import io.grpc.stub.StreamObserver;
import io.quarkus.example.PongReply;
import io.quarkus.example.PongRequest;
import io.quarkus.example.PongServiceGrpc;

@Singleton
public class GrpcPongService extends PongServiceGrpc.PongServiceImplBase {

    private String lastTraceId;

    @Override
    public void sayPong(PongRequest request, StreamObserver<PongReply> responseObserver) {
        lastTraceId = MDC.get("traceId");
        responseObserver.onNext(PongReply.newBuilder().setMessage("pong").build());
        responseObserver.onCompleted();
    }

    public String getLastTraceId() {
        return lastTraceId;
    }
}