package io.quarkus.qe.vertx.sql.test.resources;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Collections;
import java.util.List;

public class MysqlTestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "mysql";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(io.quarkus.qe.vertx.sql.test.resources.MysqlResource.class));
    }
}