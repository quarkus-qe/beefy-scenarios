package io.quarkus.qe.vertx.sql.handlers.Flights;

import io.quarkus.qe.vertx.sql.test.resources.PostgresqlResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(PostgresqlResource.class)
//@DisabledOnNativeImage("Some issues are under investigation")
public class PostgresqlHandlerIT extends FlightsHandlerTest {
}
