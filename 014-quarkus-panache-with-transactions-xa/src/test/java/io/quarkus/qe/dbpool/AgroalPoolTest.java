package io.quarkus.qe.dbpool;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.agroal.api.AgroalDataSource;
import io.quarkus.qe.containers.MysqlTestProfile;
import io.quarkus.qe.rest.data.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.tuples.Tuple2;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * The aim of these tests is verified agroal and entityManager pool management
 * Some of these tests required some extra load, in order to reproduce concurrency issues.
 */
@QuarkusTest
@TestProfile(MysqlTestProfile.class)
public class AgroalPoolTest {

    private final int CONCURRENCY_LEVEL = 20;

    @Inject
    EntityManager em;

    @Inject
    UserRepository users;

    @Inject
    AgroalDataSource agroalDataSource;

    @ConfigProperty(name = "quarkus.datasource.jdbc.idle-removal-interval")
    String idleSec;

    @ConfigProperty(name = "quarkus.datasource.jdbc.max-size")
    int datasourceMaxSize;

    @ConfigProperty(name = "quarkus.datasource.jdbc.min-size")
    int datasourceMinSize;

    @Test
    @Disabled("Pending Zulip conversation about quarkus.datasource.jdbc.idle-removal-interval")
    public void idleTimeoutTest() throws InterruptedException {
        makeApplicationQuery();
        Thread.sleep(Duration.ofMillis(getIdleMs() + 1).toMillis());
        assertEquals(1, activeConnections(), "agroalCheckIdleTimeout: Expected " + datasourceMinSize + " active connections");
    }

    @Test
    public void poolTurnoverTest() throws InterruptedException {
        final int events = 500;
        final AtomicInteger max = new AtomicInteger(0);

        AssertSubscriber<Tuple2<Long, Long>> subscriber = Multi.createBy().combining()
                .streams(makeApplicationQueryAsync(events), activeConnectionsAsync(events))
                .asTuple().subscribe().withSubscriber(AssertSubscriber.create(events));

        subscriber.awaitItems(events).getItems().forEach(t -> {
            Long activeCon = t.getItem2();
            if (max.intValue() < activeCon) {
                max.set(activeCon.intValue());
            }
            assertTrue(datasourceMaxSize >= activeCon, "More mysql SQL connections than pool max-size");
            assertTrue(datasourceMinSize <= activeCon, "Less mysql SQL connections than pool min-size");
        });
    }

    @Test
    public void borderConditionBetweenIdleAndGetConnectionTest() {
        final int events = 500;
        for (int k = 0; k < events; k++) {
            AssertSubscriber<Integer> subscriber = Multi.createFrom().range(0, CONCURRENCY_LEVEL).flatMap(n -> Multi
                    .createFrom().ticks()
                    .every(Duration.ofMillis(getIdleMs() + 3))
                    .onOverflow().drop()
                    .onFailure().invoke(e -> Assertions.fail("Unexpected exception " + e.getMessage()))
                    .onItem().transform(i -> makeApplicationQuery()))
                    .subscribe()
                    .withSubscriber(AssertSubscriber.create(CONCURRENCY_LEVEL));

            subscriber
                    .awaitItems(CONCURRENCY_LEVEL)
                    .getItems()
                    .forEach(statusCode -> assertEquals(statusCode, HttpStatus.SC_OK, "Unexpected Application response"));
        }
    }

    @Test
    public void concurrentLoadTest() {
        final int events = 100;
        for (int i = 0; i < events; i++) {
            Multi.createFrom()
                    .range(0, CONCURRENCY_LEVEL).subscribe()
                    .with(n -> assertEquals(2, users.count(), "UnexpectedUser Amount"));
        }
    }

    @Test
    public void connectionConcurrencyTest() {
        final int events = 500;
        for (int k = 0; k < events; k++) {
            AssertSubscriber<String> subscriber = Multi.createFrom().range(0, CONCURRENCY_LEVEL).flatMap(n -> Multi
                    .createFrom().ticks()
                    .every(Duration.ofMillis(getIdleMs() + 3))
                    .onOverflow().drop()
                    .onFailure().invoke(e -> Assertions.fail("Unexpected exception " + e.getMessage()))
                    .onItem().transform(i -> makeAgroalRawQuery()))
                    .subscribe()
                    .withSubscriber(AssertSubscriber.create(CONCURRENCY_LEVEL));

            subscriber
                    .awaitItems(CONCURRENCY_LEVEL)
                    .getItems()
                    .forEach(currentTime -> assertFalse(currentTime.isEmpty(), "Unexpected Application response"));
        }
    }

    private long getIdleMs() {
        int idle = Integer.parseInt(idleSec.replaceAll("[A-Z]", ""));
        return Duration.ofSeconds(idle).toMillis();
    }

    private Multi<Long> activeConnectionsAsync(int events) {
        return Multi.createFrom().range(0, events).onItem().transform(i -> activeConnections());
    }

    private Multi<Long> makeApplicationQueryAsync(int events) {
        return Multi.createFrom().range(0, events).onItem().transform(i -> {
            makeApplicationQuery();
            return 0L;
        });
    }

    private Long activeConnections() {
        Query query = em.createNativeQuery("select * from INFORMATION_SCHEMA.PROCESSLIST;");
        return (long) query.getResultList().size();
    }

    private int makeApplicationQuery() {
        return given()
                .accept("application/hal+json")
                .when().get("/users/all")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().statusCode();
    }

    private String makeAgroalRawQuery() {
        String currentTime = "";
        try (Connection con = agroalDataSource.getConnection();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT CURRENT_TIMESTAMP")) {
            rs.next();
            currentTime = rs.getString(1);
        } catch (SQLException e) {
            assertNull(e.getCause(), "makeAgroalRawQuery: Agroal datasource/poolImpl unexpected error");
        }

        return currentTime;
    }
}
