package io.quarkus.qe.vertx.sql.dbpool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.qe.vertx.sql.test.resources.PostgresqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.client.predicate.ResponsePredicate;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

import jakarta.inject.Inject;

@QuarkusTest
@TestProfile(PostgresqlTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class PostgresPoolTest extends AbstractCommons {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresPoolTest.class);

    static final int TIMEOUT_SEC = 60;
    static final int HTTP_OK = 200;
    static WebClient httpClient;

    @Inject
    PgPool postgresql;

    @ConfigProperty(name = "quarkus.http.test.port")
    int port;

    @ConfigProperty(name = "quarkus.datasource.reactive.max-size")
    int datasourceMaxSize;

    @ConfigProperty(name = "quarkus.datasource.reactive.idle-timeout")
    int idle;

    @BeforeAll
    public static void beforeAll() {
        httpClient = WebClient.create(Vertx.vertx(), new WebClientOptions());
    }

    @Test
    @DisplayName("DB connections are re-used")
    @Order(1)
    public void checkDbPoolTurnover() throws InterruptedException {
        final int events = 25000;
        CountDownLatch done = new CountDownLatch(events);

        for (int i = 0; i < events; i++) {
            Uni<Boolean> connectionsReUsed = makeHttpReq(httpClient, "airlines/", HTTP_OK).flatMap(body -> activeConnections()
                    .onItem().ifNull().failWith(() -> new RuntimeException("Oh No! no postgres active connections found!"))
                    .onItem().ifNotNull().transform(this::checkDbActiveConnections));

            connectionsReUsed.subscribe().with(reUsed -> {
                assertTrue(reUsed, "More postgres SQL connections than pool max-size property");
                done.countDown();
            });
        }

        done.await(TIMEOUT_SEC, TimeUnit.SECONDS);
        assertEquals(done.getCount(), 0, String.format("Missing %d events.", events - done.getCount()));
    }

    @Test
    @Order(2)
    @DisplayName("IDLE remove expiration time")
    public void checkIdleExpirationTime() throws InterruptedException {
        // push Db pool to the limit in order to raise the number of active connections
        final int events = 25000;
        CountDownLatch done = new CountDownLatch(events);

        for (int i = 0; i < events; i++) {
            Uni<Long> activeConnectionsAmount = makeHttpReq(httpClient, "airlines/", HTTP_OK)
                    .flatMap(body -> activeConnections()
                            .onItem().ifNull()
                            .failWith(() -> new RuntimeException("Oh No! no postgres active connections found!"))
                            .onItem().ifNotNull().transformToUni(resp -> activeConnections()));

            activeConnectionsAmount.subscribe().with(amount -> {
                // be sure that you have more than 1 connections
                assertThat(amount, greaterThanOrEqualTo(1L));
                done.countDown();
            });
        }

        done.await(TIMEOUT_SEC, TimeUnit.SECONDS);
        assertEquals(done.getCount(), 0, String.format("Missing %d events.", events - done.getCount()));

        // Make just one extra query and Hold "Idle + 1 sec" in order to release inactive connections.
        Uni<Long> activeConnectionAmount = postgresql.preparedQuery("SELECT CURRENT_TIMESTAMP").execute()
                .onItem().delayIt().by(Duration.ofSeconds(idle + 1))
                .onItem().transformToUni(resp -> activeConnections());

        CountDownLatch doneIdleExpired = new CountDownLatch(1);
        activeConnectionAmount.subscribe().with(connectionsAmount -> {
            // At this point you should just have one connection -> SELECT CURRENT_TIMESTAMP
            assertEquals(1, connectionsAmount, "Idle doesn't remove IDLE expired connections!.");
            if (connectionsAmount == 1)
                doneIdleExpired.countDown();
        });

        doneIdleExpired.await(TIMEOUT_SEC, TimeUnit.SECONDS);
        assertEquals(doneIdleExpired.getCount(), 0, "Missing doneIdleExpired query.");
    }

    @Test
    @DisplayName("Idle issue: Fail to read any response from the server, the underlying connection might get lost unexpectedly.")
    @Order(3)
    @Disabled("Takes too much time and is fixed by Vertx 4.1")
    public void checkBorderConditionBetweenIdleAndGetConnection() {
        try {
            long idleMs = TimeUnit.SECONDS.toMillis(idle);
            latch = new CountDownLatch(1); // ignore, this test will run until Timeout or get an error occurs.
            AtomicInteger at = new AtomicInteger(0);
            Handler<Long> handler = l -> {
                LOGGER.info("###################################################: ");
                Multi.createFrom().range(1, 3)
                        .concatMap(n -> {
                            LOGGER.info("Connection #" + at.incrementAndGet());
                            return postgresql.preparedQuery("SELECT CURRENT_TIMESTAMP")
                                    .execute().onFailure().invoke(error -> {
                                        LOGGER.info("Error: " + at.get());
                                        LOGGER.error("Error on query: '" + error.getMessage() + "'");
                                        latch.countDown();
                                        fail(error.getMessage());
                                    }).map(RowSet::iterator).onItem().transform(iterator -> {
                                        OffsetDateTime result = OffsetDateTime.now();
                                        if (iterator.hasNext()) {
                                            Row row = iterator.next();
                                            LOGGER.info("Result : " + at.get() + " : " + row.getOffsetDateTime(0));
                                            result = row.getOffsetDateTime(0);
                                        }
                                        return result;
                                    }).toMulti();
                        }).collect().in(ArrayList::new, List::add).subscribe().with(re -> {
                            LOGGER.info("Subscribe success: -> " + re.get(0));
                        }, Throwable::printStackTrace);
            };
            Vertx.vertx().setPeriodic(idleMs + 3, l -> handler.handle(l));
            await(5, TimeUnit.MINUTES);
        } catch (IllegalStateException ex) {
        } finally {
            assertEquals(1, latch.getCount(), "An unexpected error was thrown.");
        }
    }

    private Uni<Long> activeConnections() {
        return postgresql.query(
                "SELECT count(*) as active_con FROM pg_stat_activity where application_name like '%vertx%'")
                .execute()
                .onItem().transform(RowSet::iterator).onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getLong("active_con") : null);
    }

    protected Uni<JsonArray> makeHttpReq(WebClient httpClient, String path, int expectedStatus) {
        return httpClient.getAbs(getAppEndpoint() + path)
                .expect(ResponsePredicate.status(expectedStatus))
                .send().map(HttpResponse::bodyAsJsonArray);
    }

    protected String getAppEndpoint() {
        return String.format("http://localhost:%d/", port);
    }

    private boolean checkDbActiveConnections(long active) {
        return active <= datasourceMaxSize + (7); // TODO: double check this condition ... this magical number is scary!.
    }

}
