package io.quarkus.qe;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;

import io.quarkus.qe.containers.ConsulTestResource;
import io.quarkus.test.QuarkusProdModeTest;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(ConsulTestResource.class)
public class GreetingResourceTest {

    protected static final String QUARKUS_PROFILE = "quarkus.profile";
    protected static final String NATIVE = "native";
    protected static final boolean IS_NATIVE = System.getProperty(QUARKUS_PROFILE, "").equals(NATIVE);
    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final String CUSTOM_PROPERTY = "my.property";
    private static final int HTTP_PORT = 8081;
    private static final int ASSERT_TIMEOUT_SECONDS = 5;

    @RegisterExtension
    static final QuarkusProdModeTest APP = new QuarkusProdModeTest()
            .setBuildNative(IS_NATIVE)
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .addClass(GreetingResource.class)
                            .addAsResource(APPLICATION_PROPERTIES, APPLICATION_PROPERTIES))
            .setRun(true);

    @Test
    public void shouldUpdateCustomProperty() {
        thenGreetingsApiReturns("Hello Default");

        whenUpdateCustomPropertyTo("Test");
        thenGreetingsApiReturns("Hello Test");
    }

    private void whenUpdateCustomPropertyTo(String newValue) {
        Consul client = Consul.builder().build();
        KeyValueClient kvClient = client.keyValueClient();
        try {
            String properties = IOUtils
                    .toString(this.getClass().getClassLoader().getResourceAsStream("application.properties"),
                            StandardCharsets.UTF_8)
                    .replace(CUSTOM_PROPERTY + "=Default", CUSTOM_PROPERTY + "=" + newValue);
            kvClient.putValue("config/app", properties);
        } catch (IOException e) {
            fail("Failed to load properties. Caused by " + e.getMessage());
        }

        APP.stop();
        APP.start();
    }

    private void thenGreetingsApiReturns(String expected) {
        Awaitility.await().atMost(Duration.ofSeconds(ASSERT_TIMEOUT_SECONDS)).untilAsserted(() -> {
            String actual = given().port(HTTP_PORT).get("/api")
                    .then().statusCode(HttpStatus.SC_OK)
                    .extract().asString();

            assertEquals(expected, actual, "Unexpected response from service");
        });
    }
}
