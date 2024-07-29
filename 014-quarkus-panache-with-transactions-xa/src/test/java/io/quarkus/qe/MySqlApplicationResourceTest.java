package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.containers.MySqlDatabaseTestResource;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@WithTestResource(MySqlDatabaseTestResource.class)
public class MySqlApplicationResourceTest {

    private static final String APPLICATION_PATH = "/application";
    private static final String DATA_SOURCE_PATH = "/data-source";

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

    @Test
    public void shouldDataSourceBeProperlyConfigured() {
        dataSourcePath().get("/default/connection-provider-class")
                .then().statusCode(HttpStatus.SC_OK).and().body(is("com.mysql.cj.jdbc.Driver"));

        dataSourcePath().get("/with-xa/connection-provider-class")
                .then().statusCode(HttpStatus.SC_OK).and().body(is("com.mysql.cj.jdbc.MysqlXADataSource"));
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

    private static final RequestSpecification dataSourcePath() {
        return given().when().basePath(DATA_SOURCE_PATH);
    }
}
