package io.quarkus.qe.vertx.sql.test.resources;

import java.util.Collections;
import java.util.List;

import io.quarkus.test.junit.QuarkusTestProfile;

public class Db2TestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "db2";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(Db2Resource.class));
    }
}
