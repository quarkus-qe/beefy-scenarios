package io.quarkus.qe.vertx.sql.test.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

public class PostgresqlTestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "postgresql";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }

}
