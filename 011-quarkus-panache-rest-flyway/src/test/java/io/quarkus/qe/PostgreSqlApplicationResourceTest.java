package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.PostgreSqlDatabaseTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
@QuarkusTestResource(PostgreSqlDatabaseTestResource.class)
public class PostgreSqlApplicationResourceTest {

    private static final String APPLICATION_PATH = "/application";

    private ApplicationEntity actualEntity;
    private ApplicationEntity[] actualList;

    @AfterEach
    public void tearDown() {
        deleteEntityIfExists();
    }

    @Test
    public void shouldCreateApplication() {
        whenCreateApplication("my-app-name");
        thenApplicationMatches("my-app-name");
    }

    @Test
    public void shouldUpdateApplication() {
        whenCreateApplication("my-app-name");
        whenUpdateApplication("another-app-name");
        thenApplicationMatches("another-app-name");
    }

    @Test
    public void shouldListApplications() {
        whenCreateApplication("my-app-name");
        whenGetApplications();
        thenApplicationsCountIs(1);
        thenApplicationsContainsAnItemWithName("my-app-name");
    }

    @Test
    public void shouldDeleteApplication() {
        whenCreateApplication("my-app-name");
        whenDeleteApplication();
        whenGetApplications();
        thenApplicationsCountIs(0);
    }

    /**
     * This test is disabled because the conflict exception raised by hibernate validator is wrapping it up by the rollback
     * exception which ends up in a HTTP 500 Internal Server Error instead of a HTTP 409 Conflict.
     */
    @Disabled("Caused by https://github.com/quarkusio/quarkus/issues/13307.")
    @Test
    public void shouldReturnBadRequestIfApplicationNameIsNull() {
        applicationPath().body(new ApplicationEntity()).post()
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    private void whenCreateApplication(String appName) {
        ApplicationEntity request = new ApplicationEntity();
        request.name = appName;
        actualEntity = applicationPath().body(request).post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and().extract().as(ApplicationEntity.class);
    }

    private void whenUpdateApplication(String appName) {
        actualEntity.name = appName;
        applicationPath().body(actualEntity).put("/" + actualEntity.id)
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private void whenDeleteApplication() {
        applicationPath().delete("/" + actualEntity.id)
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

        actualEntity = null;
    }

    private void whenGetApplications() {
        actualList = applicationPath().get().then().statusCode(HttpStatus.SC_OK).and().extract().as(ApplicationEntity[].class);
    }

    private void thenApplicationMatches(String expectedAppName) {
        assertNotNull(actualEntity.id);
        assertEquals(expectedAppName, actualEntity.name);
    }

    private void thenApplicationsCountIs(int expectedCount) {
        assertNotNull(actualList);
        assertEquals(expectedCount, actualList.length);
    }

    private void thenApplicationsContainsAnItemWithName(String expectedAppName) {
        assertNotNull(actualList);
        assertTrue(Stream.of(actualList).anyMatch(item -> expectedAppName.equals(item.name)));
    }

    private void deleteEntityIfExists() {
        if (actualEntity != null) {
            whenDeleteApplication();
        }
    }

    private static final RequestSpecification applicationPath() {
        return given().accept(MediaType.APPLICATION_JSON).contentType(ContentType.JSON).when().basePath(APPLICATION_PATH);
    }
}
