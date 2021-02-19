package io.quarkus.qe.vertx.sql.handlers.flights;

import io.quarkus.qe.vertx.sql.test.resources.MysqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(MysqlTestProfile.class)
public class MysqlFlightsHandlerTest extends FlightsHandlerTest {
}
