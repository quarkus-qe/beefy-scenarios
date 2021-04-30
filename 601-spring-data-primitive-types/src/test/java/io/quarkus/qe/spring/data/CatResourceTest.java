package io.quarkus.qe.spring.data;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.qe.spring.data.model.Cat;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

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

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/13015
    @Test
    void testFindCatsByDeathReason() {
        Response response = when().get("/cat/findCatsByMappedSuperclassField/covid19").then()
                .statusCode(200)
                .contentType(ContentType.JSON).extract().response();

        List<Cat> cats = Arrays.asList(response.getBody().as(Cat[].class));
        cats.stream().forEach(cat -> assertThat(cat.getDeathReason(), is("covid19")));
    }
}
