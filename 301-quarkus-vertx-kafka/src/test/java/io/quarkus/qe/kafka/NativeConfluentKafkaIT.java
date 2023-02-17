package io.quarkus.qe.kafka;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class NativeConfluentKafkaIT extends ConfluentKafkaTest {

    @Override
    protected boolean isNativeTest() {
        return true;
    }
}
