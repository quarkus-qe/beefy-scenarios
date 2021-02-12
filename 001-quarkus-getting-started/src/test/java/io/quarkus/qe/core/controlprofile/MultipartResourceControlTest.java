package io.quarkus.qe.core.controlprofile;

import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@DisabledOnNativeImage
@TestProfile(MultipartResourceControlTestProfile.class)
public class MultipartResourceControlTest {

    public static final String TEXT = "Přikrášlený žloťoučký kůň úpěl ďábelské ódy.";

    @Test
    public void testMultipartText() {
        MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(TEXT)
                .controlName("text")
                .header("Content-Type", "text/plain")
                .charset(StandardCharsets.UTF_8)
                .build();
        given().multiPart(multiPartSpecification)
                .post("/multipart/text")
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body(not(equalTo(TEXT)));
    }

}
