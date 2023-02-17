package io.quarkus.qe.core;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@Disabled("TODO: Caused by REST endpoints are not exposed in Native")
@QuarkusIntegrationTest
public class NativeAlmostAllQuarkusExtensionsIT extends AlmostAllQuarkusExtensionsTest {

    // Execute the same tests but in native mode.
}