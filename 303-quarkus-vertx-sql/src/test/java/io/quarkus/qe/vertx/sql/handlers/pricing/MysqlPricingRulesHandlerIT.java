package io.quarkus.qe.vertx.sql.handlers.pricing;

import io.quarkus.qe.vertx.sql.test.profiles.MysqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(MysqlTestProfile.class)
public class MysqlPricingRulesHandlerIT extends PricingRulesHandlerTest {
}
