package io.quarkus.qe.kafka;

import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@DisabledOnNativeImage("Github Ref: https://github.com/quarkusio/quarkus/issues/18000")
public class SaslApacheKafkaClientTestIT extends SaslApacheKafkaClientTest {
}
