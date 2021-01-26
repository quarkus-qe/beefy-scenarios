package io.quarkus.qe;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.openapi.runtime.io.Format;

@QuarkusTest
public class OpenApiTest {

    private static String directory = "target/generated/jax-rs/";
    private static final String OPEN_API_DOT = "openapi.";

    @Test
    @DisplayName("QUARKUS-716")
    public void testOpenApiPathAccessResource() {
        Path json = Paths.get(directory, OPEN_API_DOT + Format.JSON.toString().toLowerCase());
        Assertions.assertTrue(Files.exists(json));
        Path yaml = Paths.get(directory, OPEN_API_DOT + Format.YAML.toString().toLowerCase());
        Assertions.assertTrue(Files.exists(yaml));
    }
}
