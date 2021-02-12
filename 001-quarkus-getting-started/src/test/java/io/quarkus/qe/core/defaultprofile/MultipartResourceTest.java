package io.quarkus.qe.core.defaultprofile;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.MultiPartSpecification;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class MultipartResourceTest {

    private static final String FILE_NAME = "/quarkus.png";
    private static final String TEXT = "Přikrášlený žloťoučký kůň úpěl ďábelské ódy.";
    private static File file;
    private static String fileContent;
    private static InputStream data;

    @BeforeAll
    public static void beforeAll() throws IOException {
        file = new File(MultipartResourceTest.class.getResource(FILE_NAME).getFile());
        fileContent = IOUtils.toString(MultipartResourceTest.class.getResourceAsStream(FILE_NAME), StandardCharsets.UTF_8);
    }

    @BeforeEach
    public void beforeEach() {
        data = IOUtils.toInputStream(TEXT, StandardCharsets.UTF_8);
    }

    @Test
    public void testMultipart() {
        whenSendMultipartData("/multipart")
                .contentType("multipart/form-data");
    }

    @Test
    public void testMultipartText() {
        whenSendMultipartData("/multipart/text")
                .contentType(ContentType.TEXT)
                .body(equalTo(TEXT));
    }

    @Test
    public void testMultipartFile() {
        whenSendMultipartData("/multipart/file")
                .contentType(ContentType.TEXT)
                .body(equalTo(fileContent));
    }

    @Test
    public void testMultipartData() {
        whenSendMultipartData("/multipart/data")
                .contentType(ContentType.TEXT)
                .body(equalTo(TEXT));
    }

    private ValidatableResponse whenSendMultipartData(String path) {
        MultiPartSpecification textSpec = new MultiPartSpecBuilder(TEXT)
                .controlName("text")
                .mimeType("text/plain")
                .charset(StandardCharsets.UTF_8)
                .build();
        MultiPartSpecification dataSpec = new MultiPartSpecBuilder(data)
                .controlName("data")
                .header("Content-Type", "application/octet-stream")
                .build();
        return given()
                .contentType("multipart/form-data")
                .multiPart(textSpec)
                .multiPart("file", file, "image/png")
                .multiPart(dataSpec)
                .post(path)
                .then()
                .statusCode(200);
    }
}
