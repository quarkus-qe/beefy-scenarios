package io.quarkus.qe.vertx.sql.handlers.airport;

import io.quarkus.qe.vertx.sql.test.resources.PostgresqlResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(PostgresqlResource.class)
public class PostgresqlAirportHandlerIT extends AirportHandlerTest {
}
