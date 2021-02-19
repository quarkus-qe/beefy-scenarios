package io.quarkus.qe.vertx.sql.handlers.flights;


import io.quarkus.qe.vertx.sql.test.resources.Db2TestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Db2TestProfile.class)
public class Db2FlightsHandlerTest extends FlightsHandlerTest {
}
