package io.quarkus.qe.kafka;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@Disabled("https://github.com/quarkusio/quarkus/issues/25143")
public class NativeConfluentKafkaIT extends ConfluentKafkaTest {

    @Override
    protected boolean isNativeTest() {
        return true;
    }
}
