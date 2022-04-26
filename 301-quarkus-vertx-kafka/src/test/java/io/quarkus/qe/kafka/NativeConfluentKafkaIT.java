package io.quarkus.qe.kafka;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeConfluentKafkaIT extends ConfluentKafkaTest {

    @Override
    protected boolean isNativeTest() {
        return true;
    }
}
