package io.quarkus.qe.kafka;

import io.quarkus.qe.kafka.resources.StrimziKafkaResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(StrimziKafkaResource.class)
public class AlertMonitorIT extends AlertMonitorTest {
}
