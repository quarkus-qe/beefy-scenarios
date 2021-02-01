package io.quarkus.qe.containers;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.impl.client.HttpClients;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String OIDC_AUTH_URL_PROPERTY = "quarkus.oidc.auth-server-url";
    private static final String OIDC_CLIENT_AUTH_URL_PROPERTY = "quarkus.oidc-client.auth-server-url";

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    private static final String REALM = "test-realm";
    private static final int PORT = 8080;

    private static final String REALM_FILE = "/tmp/realm.json";
    private static final String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:11.0.3";

    private GenericContainer<?> container;

    @SuppressWarnings("resource")
    @Override
    public Map<String, String> start() {

        container = new GenericContainer<>(KEYCLOAK_IMAGE)
                .withEnv("KEYCLOAK_USER", USER)
                .withEnv("KEYCLOAK_PASSWORD", PASSWORD)
                .withEnv("KEYCLOAK_IMPORT", REALM_FILE)
                .withClasspathResourceMapping("test-realm.json", REALM_FILE, BindMode.READ_ONLY)
                .waitingFor(Wait.forHttp("/auth").withStartupTimeout(Duration.ofMinutes(5)));
        container.addExposedPort(PORT);
        container.start();

        Map<String, String> properties = new HashMap<>();
        properties.put(OIDC_AUTH_URL_PROPERTY, oidcAuthUrl());
        properties.put(OIDC_CLIENT_AUTH_URL_PROPERTY, oidcAuthUrl());

        return properties;
    }

    @Override
    public void stop() {
        Optional.ofNullable(container).ifPresent(GenericContainer::stop);
    }

    @Override
    public void inject(Object testInstance) {
        Class<?> c = testInstance.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if (f.getType().isAssignableFrom(AuthzClient.class)) {
                    setFieldValue(f, testInstance, initAuthzClient());
                }
            }
            c = c.getSuperclass();
        }
    }

    private AuthzClient initAuthzClient() {
        return AuthzClient.create(new Configuration(
                keycloakUrl(),
                REALM,
                "test-application-client",
                Collections.singletonMap("secret", "test-application-client-secret"),
                HttpClients.createDefault()));
    }

    private String keycloakUrl() {
        return String.format("http://localhost:%s/auth", container.getMappedPort(PORT));
    }

    private String oidcAuthUrl() {
        return String.format("%s/realms/%s", keycloakUrl(), REALM);
    }

    private void setFieldValue(Field f, Object testInstance, Object value) {
        try {
            f.setAccessible(true);
            f.set(testInstance, value);
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
