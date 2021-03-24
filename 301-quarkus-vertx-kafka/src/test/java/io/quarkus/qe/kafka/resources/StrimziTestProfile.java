package io.quarkus.qe.kafka.resources;

import io.quarkus.qe.kafka.resources.StrimziKafkaResource;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.List;

public class StrimziTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "strimzi";
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(StrimziKafkaResource.class));
    }
}
