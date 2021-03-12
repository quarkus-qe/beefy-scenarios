package io.quarkus.qe.vertx.sql.test.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

public class Db2TestProfile implements QuarkusTestProfile {

    public static final String PROFILE = "db2";

    @Override
    public String getConfigProfile() {
        return PROFILE;
    }
}
