package io.quarkus.qe.vertx.sql.handlers.flights;

import io.quarkus.qe.vertx.sql.test.profiles.PostgresqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(PostgresqlTestProfile.class)
public class PostgresqlFlightsHandlerIT extends FlightsHandlerTest {
}
