package io.quarkus.qe.vertx.sql.test.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

public class MysqlTestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "mysql";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }
}