package io.quarkus.qe.hibernate.validator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.TestResourceScope;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@WithTestResource(value = H2DatabaseTestResource.class, scope = TestResourceScope.RESTRICTED_TO_CLASS)
public class JakartaPersistenceAndHibernateValidatorResourceTest {

    @Test
    public void testJakartaPersistenceAndHibernateValidatorEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .body(containsString("hello"))
                .body(not(containsString("HV000041")));

        // second request is where the issue appears
        given()
                .when().get("/hello")
                .then()
                .body(containsString("hello"))
                .body(not(containsString("HV000041")))
                .body(not(containsString("HV000")));
    }

}
