package io.quarkus.qe.vertx.webclient;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.NativeImageTest;

@Disabled("https://github.com/quarkusio/quarkus/issues/22275")
@NativeImageTest
public class ChuckNorrisResourceIT extends ChuckNorrisResourceTest {

    private static final String DEBUG_SYMBOLS_FILE_NAME = "300-quarkus-vertx-webclient-1.0.0-SNAPSHOT-runner.debug";

    @Test
    public void checkNativeDebugSymbols() {
        File debugFile = Paths.get("target", DEBUG_SYMBOLS_FILE_NAME).toFile();
        assertTrue("Missing debug symbols file: " + DEBUG_SYMBOLS_FILE_NAME, debugFile.exists());
    }
}
