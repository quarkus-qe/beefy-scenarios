package io.quarkus.qe.spring.data;

import io.quarkus.qe.spring.data.model.Cat;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class CatResourceTest {
    @Test
    void testCustomFindPublicationYearObjectBoolean() {
        when().get("/cat/customFindDistinctiveObject/2").then()
                .statusCode(200)
                .body(is("true"));
    }
    @Test
    void testCustomFindPublicationYearPrimitiveBoolean() {
        when().get("/cat/customFindDistinctivePrimitive/2").then()
                .statusCode(200)
                .body(is("true"));
    }
    // QUARKUS-532
    @Test
    void testFindCatsByDeathReason() {
        Response response = when().get("/cat/findCatsByMappedSuperclassField/covid19").then()
                .statusCode(200)
                .contentType(ContentType.JSON).extract().response();

        List<Cat> cats = Arrays.asList(response.getBody().as(Cat[].class));
        cats.stream().forEach(cat -> assertThat(cat.getDeathReason(), is("covid19")));
    }
}
