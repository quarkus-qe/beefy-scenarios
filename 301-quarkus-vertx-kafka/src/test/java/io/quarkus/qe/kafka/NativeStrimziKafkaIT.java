package io.quarkus.qe.kafka;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class NativeStrimziKafkaIT extends StrimziKafkaTest {

    @Override
    protected boolean isNativeTest() {
        return true;
    }
}
