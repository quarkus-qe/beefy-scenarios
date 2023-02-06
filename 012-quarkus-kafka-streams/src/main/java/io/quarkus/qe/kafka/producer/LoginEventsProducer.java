package io.quarkus.qe.kafka.producer;

import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.quarkus.qe.kafka.model.LoginAttempt;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import io.vertx.core.json.Json;

@ApplicationScoped
public class LoginEventsProducer {

    private static final Logger LOG = Logger.getLogger(LoginEventsProducer.class);

    @ConfigProperty(name = "producer.httpCodes")
    List<Integer> HTTP_CODES;

    @ConfigProperty(name = "producer.loginUrls")
    List<String> LOGIN_URLs;

    private final Random random = new Random();

    @Outgoing("login-http-response-values")
    public Multi<Record<String, String>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(100))
                .onOverflow().drop()
                .map(tick -> {
                    String loginEndpoint = getRandomEndpointUrl();
                    String loginEndpointEnc = encodeId(loginEndpoint);
                    Integer httpCode = getRandomHttpCode();

                    LOG.infov("Endpoint: {0} ID: {1}, HTTP-code: {2}", loginEndpoint, loginEndpointEnc, httpCode);
                    return Record.of(loginEndpointEnc,
                            Json.encode(new LoginAttempt(loginEndpointEnc, loginEndpoint, httpCode)));
                });
    }

    private String getRandomEndpointUrl() {
        return LOGIN_URLs.get(random.nextInt(LOGIN_URLs.size()));
    }

    private Integer getRandomHttpCode() {
        return HTTP_CODES.get(random.nextInt(HTTP_CODES.size()));
    }

    private String encodeId(final String id) {
        return Base64.getEncoder().encodeToString(id.getBytes());
    }

}
