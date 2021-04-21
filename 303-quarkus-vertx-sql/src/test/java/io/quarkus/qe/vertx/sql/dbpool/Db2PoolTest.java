package io.quarkus.qe.vertx.sql.dbpool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.qe.vertx.sql.test.resources.Db2TestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.mutiny.Multi;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.db2client.DB2Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

@Disabled("Caused by https://github.com/quarkusio/quarkus/issues/14608")
@QuarkusTest
@TestProfile(Db2TestProfile.class)
public class Db2PoolTest extends AbstractCommons {

    private static final int ASSERT_TIMEOUT_MINUTES = 5;
    private static final int THREE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(Db2PoolTest.class);

    @ConfigProperty(name = "quarkus.datasource.db2.reactive.idle-timeout")
    int idle;

    @Inject
    @Named("db2")
    DB2Pool db2;

    @Test
    @DisplayName("Idle issue: Fail to read any response from the server, the underlying connection might get lost unexpectedly.")
    public void checkBorderConditionBetweenIdleAndGetConnection() {
        try {
            long idleMs = TimeUnit.SECONDS.toMillis(idle);
            latch = new CountDownLatch(1); // ignore, this test will run until Timeout or get an error occurs.
            AtomicInteger at = new AtomicInteger(0);
            Handler<Long> handler = l -> {
                LOGGER.info("###################################################: ");
                Multi.createFrom().range(1, THREE)
                        .concatMap(n -> {
                            LOGGER.info("Connection #" + at.incrementAndGet());
                            return db2.preparedQuery("SELECT CURRENT TIMESTAMP result FROM sysibm.sysdummy1")
                                    .execute().onFailure().invoke(error -> {
                                        LOGGER.info("Error: " + at.get());
                                        LOGGER.error("Error on query: '" + error.getMessage() + "'");
                                        latch.countDown();
                                        fail(error.getMessage());
                                    }).map(RowSet::iterator).onItem().transform(iterator -> {
                                        LocalDateTime result = LocalDateTime.now();
                                        if (iterator.hasNext()) {
                                            Row row = iterator.next();
                                            LOGGER.info("Result : " + at.get() + " : " + row.getLocalDateTime(0));
                                            result = row.getLocalDateTime(0);
                                        }
                                        return result;
                                    }).toMulti();
                        }).collect().in(ArrayList::new, List::add).subscribe().with(re -> {
                    LOGGER.info("Subscribe success: -> " + re.get(0));
                }, Throwable::printStackTrace);
            };
            Vertx.vertx().setPeriodic(idleMs + THREE, l -> handler.handle(l));
            await(ASSERT_TIMEOUT_MINUTES, TimeUnit.MINUTES);
            assertEquals(1, latch.getCount(), "An unexpected error was thrown.");
        } catch (IllegalStateException ex) {
        } finally {
            assertEquals(1, latch.getCount(), "An unexpected error was thrown.");
        }
    }
}
