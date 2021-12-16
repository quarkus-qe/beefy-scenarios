package io.quarkus.qe.kafka;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@Disabled("https://github.com/quarkusio/quarkus/issues/22275")
@NativeImageTest
public class NativeConfluentKafkaIT extends ConfluentKafkaTest {

    @Override
    protected boolean isNativeTest() {
        return true;
    }
}
