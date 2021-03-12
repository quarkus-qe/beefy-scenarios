package io.quarkus.qe.vertx.sql.handlers.flights;

import io.quarkus.qe.vertx.sql.test.profiles.MysqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(MysqlTestProfile.class)
public class MysqlFlightsHandlerIT extends FlightsHandlerTest {
}
