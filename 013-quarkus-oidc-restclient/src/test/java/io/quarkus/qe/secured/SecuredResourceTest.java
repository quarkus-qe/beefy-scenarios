package io.quarkus.qe.secured;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SecuredResourceTest {

    private static final String SECURED_PATH = "/secured";
    private static final String CLAIMS_FROM_BEANS_PATH = "/getClaimsFromBeans";
    private static final String CLAIMS_FROM_TOKEN_PATH = "/getClaimsFromToken";

    @Test
    public void verifySecuredEndpointIsProtected() {
        given().get(SECURED_PATH + CLAIMS_FROM_BEANS_PATH)
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void verifyClaimsAreTheSame() {
        String claimsFromBeans = getClaimsFromBeans();
        String claimsFromToken = getClaimsFromToken();

        assertEquals(claimsFromBeans, claimsFromToken, "Claims should be equal");
    }

    private String getClaimsFromBeans() {
        return getClaimsInstancesFromPath(CLAIMS_FROM_BEANS_PATH);
    }

    private String getClaimsFromToken() {
        return getClaimsInstancesFromPath(CLAIMS_FROM_TOKEN_PATH);
    }

    private String getClaimsInstancesFromPath(String path) {
        String token = given().when().get("/generate-token/test-user").then().statusCode(200).extract().asString();

        return given()
                .auth().preemptive().oauth2(token)
                .get(SECURED_PATH + path)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }

}
