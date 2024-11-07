package io.quarkus.qe.hibernate.items;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.TestResourceScope;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@WithTestResource(value = H2DatabaseTestResource.class, scope = TestResourceScope.RESTRICTED_TO_CLASS)
public class ItemsResourceTest {

    /**
     * Required data is pulled in from the `import.sql` resource.
     */
    @Test
    public void shouldNotFailWithConstraints() {
        given().when().get("/items/count").then().body(is("1"));
    }
}
