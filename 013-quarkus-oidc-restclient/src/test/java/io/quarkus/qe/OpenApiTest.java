package io.quarkus.qe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.openapi.runtime.io.Format;
import io.vertx.core.json.JsonObject;

@QuarkusTest
public class OpenApiTest {

    private static String directory = "target/generated/jax-rs/";
    private static final String OPEN_API_DOT = "openapi.";

    private static final String YAML = Format.YAML.toString().toLowerCase();
    private static final String YAML_FILE_NAME = OPEN_API_DOT + YAML;
    private static final Path YAML_FILE_NAME_FULL_PATH = Paths.get(directory + YAML_FILE_NAME);

    private static final String JSON = Format.JSON.toString().toLowerCase();
    private static final String JSON_FILE_NAME = OPEN_API_DOT + JSON;
    private static final Path JSON_FILE_NAME_FULL_PATH = Paths.get(directory + JSON_FILE_NAME);

    private static final String EXPECTED_TAGS = "[{\"name\":\"Ping\",\"description\":\"Ping API\"},{\"name\":\"Pong\",\"description\":\"Pong API\"}]";
    private static final String EXPECTED_INFO = "{\"title\":\"013-quarkus-oidc-restclient API\",\"version\":\"1.0.0-SNAPSHOT\"}";

    // QUARKUS-716
    @Test
    public void testJsonOpenApiPathAccessResource() throws IOException, URISyntaxException {
        assertTrue(Files.exists(JSON_FILE_NAME_FULL_PATH), JSON_FILE_NAME_FULL_PATH + " doesn't exist.");
        assertContent(toJson(JSON_FILE_NAME_FULL_PATH));
    }

    // QUARKUS-716
    @Test
    public void testYamlOpenApiPathAccessResource() throws IOException, URISyntaxException {
        assertTrue(Files.exists(YAML_FILE_NAME_FULL_PATH), YAML_FILE_NAME_FULL_PATH + " doesn't exist.");
        assertContent(toJson(YAML_FILE_NAME_FULL_PATH));
    }

    private JsonObject toJson(Path resourceFileName) throws IOException {
        URI resourceUri = resourceFileName.toUri();
        return (fileExtension(resourceUri.getPath()).equals(YAML)) ? fromYaml(resourceUri) : fromJson(resourceUri);
    }

    private JsonObject fromYaml(URI openApiYaml) throws IOException {
        return JsonObject.mapFrom(new Yaml().loadAs(openApiYaml.toURL().openStream(), Map.class));
    }

    private JsonObject fromJson(URI openApiJson) throws IOException {
        String jsonStr = IOUtils.toString(new FileReader(openApiJson.getPath()));
        return new JsonObject(jsonStr);
    }

    private String fileExtension(String uri) {
        return FilenameUtils.getExtension(uri);
    }

    private void assertContent(JsonObject content) {
        assertThat(content.getJsonArray("tags").encode(), is(EXPECTED_TAGS));
        assertThat(content.getJsonObject("info").encode(), is(EXPECTED_INFO));
        assertTrue(content.getJsonObject("components").getJsonObject("schemas").containsKey("Score"),
                "Expected component.schema.Score object.");
        assertTrue(content.getJsonObject("paths").containsKey("/rest-ping"), "Missing expected path: /rest-ping");
        assertTrue(content.getJsonObject("paths").containsKey("/rest-pong"), "Missing expected path: /rest-pong");
    }
}
