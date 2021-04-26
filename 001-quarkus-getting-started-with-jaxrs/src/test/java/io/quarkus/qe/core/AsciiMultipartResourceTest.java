package io.quarkus.qe.core;

import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
@DisabledOnNativeImage // @TestProfile works in JVM mode
@TestProfile(AsciiMultipartResourceTest.AsciiTestProfile.class)
public class AsciiMultipartResourceTest {

    public static final String TEXT_WITH_DIACRITICS = "Přikrášlený žloťoučký kůň úpěl ďábelské ódy.";
    private static final String EXPECTED_ASCII_TEXT = new String(TEXT_WITH_DIACRITICS.getBytes(StandardCharsets.UTF_8), StandardCharsets.US_ASCII);

    @Test
    public void testMultipartText() {
        MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(TEXT_WITH_DIACRITICS)
                .controlName("text")
                .header("Content-Type", "text/plain")
                .charset(StandardCharsets.UTF_8)
                .build();

        given().multiPart(multiPartSpecification)
                .post("/multipart/text")
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body(not(equalTo(TEXT_WITH_DIACRITICS)))
                .body(equalTo(EXPECTED_ASCII_TEXT));
    }

    public static class AsciiTestProfile implements QuarkusTestProfile {
        @Override
        public String getConfigProfile() {
            return "ascii";
        }
    }
}
