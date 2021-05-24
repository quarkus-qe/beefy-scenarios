package io.quarkus.qe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Disabled;

import io.quarkus.grpc.GrpcService;
import io.quarkus.qe.pong.GrpcPongService;
import io.quarkus.test.junit.QuarkusTest;

@Disabled("Caused by https://github.com/quarkusio/quarkus/issues/13224")
@QuarkusTest
public class GrpcPingPongResourceTest extends AbstractPingPongResourceTest {

    @Inject
    @GrpcService
    GrpcPongService pongService;

    @Override
    protected String endpointPrefix() {
        return "grpc";
    }

    @Override
    protected void assertTraceIdWithPongService(String expected) {
        assertEquals(expected, pongService.getLastTraceId());
    }
}
