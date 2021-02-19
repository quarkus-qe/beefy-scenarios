package io.quarkus.qe.vertx.sql.handlers.airline;

import io.quarkus.qe.vertx.sql.test.resources.MysqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(MysqlTestProfile.class)
public class MysqlAirlineHandlerTest extends AirlineHandlerTest {
}
