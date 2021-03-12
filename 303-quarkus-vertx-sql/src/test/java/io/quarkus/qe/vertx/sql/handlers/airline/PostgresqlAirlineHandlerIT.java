package io.quarkus.qe.vertx.sql.handlers.airline;

import io.quarkus.qe.vertx.sql.test.profiles.PostgresqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(PostgresqlTestProfile.class)
public class PostgresqlAirlineHandlerIT extends AirlineHandlerTest{
}
