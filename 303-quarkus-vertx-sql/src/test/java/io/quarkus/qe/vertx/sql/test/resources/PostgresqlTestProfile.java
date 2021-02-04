package io.quarkus.qe.vertx.sql.test.resources;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Collections;
import java.util.List;

public class PostgresqlTestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "postgresql";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(PostgresqlResource.class));
    }
}
