package io.quarkus.qe.spring.data;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@QuarkusTest
public class CommonsHeadersTest {
    // QUARKUS-547
    @Test
    @DisplayName("prototype scope: x-count header must be equal to request amount")
    void testRequestAmount(){
        Set<String> xCount = new HashSet<>();

        for(int index = 0; index < 3; index++){
            Headers headers =  when().get("/cat/customFindDistinctivePrimitive/2").headers();
            xCount.addAll(headers.getValues("x-count"));
        }

        assertThat("Unexpected x-count header value(Spring Prototype Scope). Must be the same as HTTP request amount.", xCount.size(), is(3));
    }
    // QUARKUS-547
    @Test
    @DisplayName("Singleton scope: x-instance-id header must be the same")
    void testInstanceId(){
        Set<String> instanceIds = new HashSet<>();
        for(int index = 0; index < 3; index++){
            Headers headers =  when().get("/cat/customFindDistinctivePrimitive/2").headers();
            instanceIds.addAll(headers.getValues("x-instance"));
        }

        assertThat("Unexpected x-instance header value(Spring Singleton Scope). Must be 1", instanceIds.size(), is(1));
    }
    // QUARKUS-547
    @Test
    @DisplayName("request scope: x-request header must be equal to request amount")
    public void testRequestScope() {
        Set<String> requestIds = new HashSet<>();
        for(int index = 0; index < 3; index++){
            Headers headers =  when().get("/cat/customFindDistinctivePrimitive/2").headers();
            requestIds.addAll(headers.getValues("x-request"));
        }

        assertThat("Unexpected x-request header value(Spring Request Scope). Must be the same as HTTP request amount.", requestIds.size(), is(3));
    }
    // QUARKUS-547
    @Test
    @DisplayName("session scope")
    public void testSessionScope(){
        final Response first = when().get("/cat/customFindDistinctivePrimitive/2");
        final String sessionId = first.sessionId();
        Headers headers = RestAssured.given()
                .sessionId(sessionId)
                .get("/cat/customFindDistinctivePrimitive/2")
                .headers();
        String msg = "Unexpected x-session header value(Spring Session Scope). Two request from the same http session must have the same x-session header.";
        assertEquals(first.header("x-session"), headers.getValue("x-session"), msg);

        headers = RestAssured.given()
                .sessionId(sessionId)
                .get("/cat/customFindDistinctivePrimitive/2")
                .headers();

        assertEquals(first.header("x-session"), headers.getValue("x-session"), msg);

        RestAssured.given()
                .sessionId(sessionId)
                .when()
                .put("/session/invalidate")
                .then();

        Response second  = RestAssured.given()
                .sessionId(sessionId)
                .get("/cat/customFindDistinctivePrimitive/2");

        assertNotEquals( first.header("x-session"), second.header("x-session"), "First http session can't be the same as second http session");
        assertNotEquals( sessionId, second.sessionId(), "(Spring Session scope) Two request from different sessions must have different x-session header.");
    }
}
