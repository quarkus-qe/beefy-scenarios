package io.quarkus.qe.vertx.sql.handlers.Airline;

import io.quarkus.qe.vertx.sql.test.resources.MysqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;


@QuarkusTest
@TestProfile(MysqlTestProfile.class)
public class MysqlHandlerTest extends AirlineHandlerTest {
}
