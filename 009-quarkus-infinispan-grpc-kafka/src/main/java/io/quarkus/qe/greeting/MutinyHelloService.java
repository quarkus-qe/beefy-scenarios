package io.quarkus.qe.greeting;

import javax.inject.Singleton;

import io.quarkus.example.dto.HelloReply;
import io.quarkus.example.dto.HelloRequest;
import io.quarkus.example.mutiny.MutinyGreeterGrpc;
import io.smallrye.mutiny.Uni;

@Singleton
public class MutinyHelloService extends MutinyGreeterGrpc.GreeterImplBase {

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        String name = request.getName();
        String message = "Hello " + name;
        return Uni.createFrom().item(HelloReply.newBuilder().setMessage(message).build());
    }
}