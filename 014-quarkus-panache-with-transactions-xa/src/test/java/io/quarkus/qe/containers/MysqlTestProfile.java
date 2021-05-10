package io.quarkus.qe.containers;

import java.util.Collections;
import java.util.List;

import io.quarkus.test.junit.QuarkusTestProfile;

public class MysqlTestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "mysql_pool_test";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(MySqlDatabaseTestResource.class));
    }
}
