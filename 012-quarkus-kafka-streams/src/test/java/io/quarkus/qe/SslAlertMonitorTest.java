package io.quarkus.qe;

import io.quarkus.qe.resources.SslStrimziKafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(SslStrimziKafkaTestResource.class)
public class SslAlertMonitorTest extends BaseAlertMonitorTest {
}
