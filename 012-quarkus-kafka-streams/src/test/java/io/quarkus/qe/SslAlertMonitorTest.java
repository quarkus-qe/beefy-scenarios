package io.quarkus.qe;

import org.junit.jupiter.api.Tag;

import io.quarkus.qe.resources.SslStrimziKafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(SslStrimziKafkaTestResource.class)
// remove this once https://github.com/quarkusio/quarkus/pull/19477
// has been included in all versions you want to support
@Tag("io.quarkus.test.junit.QuarkusTest")
public class SslAlertMonitorTest extends BaseAlertMonitorTest {
}
