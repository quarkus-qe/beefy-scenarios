package io.quarkus.qe.hibernate.items;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ItemsResourceTest {

    /**
     * Required data is pulled in from the `import.sql` resource.
     */
    @Test
    public void shouldNotFailWithConstraints() {
        given().when().get("/items/count").then().body(is("1"));
    }
}
