package io.quarkus.qe.core.containers;

import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String OIDC_AUTH_URL_PROPERTY = "quarkus.oidc.auth-server-url";

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    private static final String REALM = "test-realm";
    private static final int PORT = 8080;

    private static final String REALM_FILE = "/test-realm.json";
    private static final String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.0";

    private GenericContainer<?> container;

    @SuppressWarnings("resource")
    @Override
    public Map<String, String> start() {
        container = new GenericContainer<>(KEYCLOAK_IMAGE)
                .withEnv("KEYCLOAK_ADMIN", USER)
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", PASSWORD)
                .withClasspathResourceMapping(REALM_FILE, "/opt/keycloak" + REALM_FILE, BindMode.READ_ONLY)
                .withCommand("start-dev --import-realm --hostname-strict=false")
                .waitingFor(Wait.forLogMessage(".*Keycloak.*started.*", 1));

        container.addExposedPort(PORT);
        container.start();

        String keycloakServerUrl = "http://localhost:" + container.getMappedPort(PORT);
        return Map.of(OIDC_AUTH_URL_PROPERTY, keycloakServerUrl + "/realms/" + REALM);
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }
}
