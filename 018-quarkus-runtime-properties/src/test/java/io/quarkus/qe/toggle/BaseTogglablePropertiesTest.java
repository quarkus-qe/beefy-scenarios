package io.quarkus.qe.toggle;

import static io.restassured.RestAssured.given;

import java.time.Duration;

import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public abstract class BaseTogglablePropertiesTest {

    protected abstract void whenChangeServiceAtRuntime(TogglableServices service, boolean enable);

    @ParameterizedTest
    @EnumSource(TogglableServices.class)
    public void shouldBeUpAndRunning(TogglableServices service) {
        thenServiceIsRunning(service);

        whenDisableServiceAtRuntime(service);
        thenServiceIsNotRunning(service);

        whenEnableServiceAtRuntime(service);
        thenServiceIsRunning(service);
    }

    private void whenDisableServiceAtRuntime(TogglableServices service) {
        whenChangeServiceAtRuntime(service, false);
    }

    private void whenEnableServiceAtRuntime(TogglableServices service) {
        whenChangeServiceAtRuntime(service, true);
    }

    private void thenServiceIsRunning(TogglableServices service) {
        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            given().port(8081).get(service.getEndpoint())
                    .then().statusCode(HttpStatus.SC_OK);
        });
    }

    private void thenServiceIsNotRunning(TogglableServices service) {
        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            given().port(8081).get(service.getEndpoint())
                    .then().statusCode(HttpStatus.SC_NOT_FOUND);
        });
    }
}
