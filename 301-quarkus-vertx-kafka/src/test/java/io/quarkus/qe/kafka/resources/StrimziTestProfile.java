package io.quarkus.qe.kafka.resources;

import java.util.Collections;
import java.util.List;

import io.quarkus.test.junit.QuarkusTestProfile;

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
