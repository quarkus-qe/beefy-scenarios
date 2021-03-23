package io.quarkus.qe.greeting;

import io.grpc.stub.StreamObserver;
import io.quarkus.example.blocking.GreeterGrpc;
import io.quarkus.example.dto.HelloReply;
import io.quarkus.example.dto.HelloRequest;
import javax.inject.Singleton;



@Singleton
public class BlockingHelloService extends GreeterGrpc.GreeterImplBase{

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String name = request.getName();
        String message = "Hello " + name;
        responseObserver.onNext(HelloReply.newBuilder().setMessage(message).build());
        responseObserver.onCompleted();
    }
}