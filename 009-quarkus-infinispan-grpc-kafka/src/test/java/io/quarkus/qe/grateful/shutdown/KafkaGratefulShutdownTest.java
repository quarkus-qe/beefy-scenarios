package io.quarkus.qe.grateful.shutdown;

import static io.restassured.RestAssured.post;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.jboss.logmanager.Level;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.LogFile;
import io.quarkus.test.QuarkusProdModeTest;

public class KafkaGratefulShutdownTest {

    private static final int TOTAL_MESSAGES = 10;
    private static final String GRATEFUL_SHUTDOWN_PROPERTY = "mp.messaging.incoming.slow.graceful-shutdown";
    private static final String KAFKA_LOG_PROPERTY = "quarkus.log.category.\"io.smallrye.reactive.messaging.kafka\".level";
    private static final String LAST_MESSAGE_LOG = "Processed Message " + TOTAL_MESSAGES;
    private static final String SHUTDOWN_KAFKA_LOG = "Shutting down - Waiting for message processing to complete";

    @RegisterExtension
    static QuarkusProdModeTest app = new QuarkusProdModeTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .addClasses(SlowTopicResource.class, SlowTopicConsumer.class))
            .setRuntimeProperties(Collections.singletonMap("quarkus.http.port", "8083"))
            .setLogFileName("out.log")
            .overrideConfigKey(KAFKA_LOG_PROPERTY, Level.DEBUG.getName())
            .setRun(true);

    @LogFile
    Path logfile;

    @Test
    public void shouldWaitForMessagesWhenGratefulShutdownIsEnabled() {
        givenApplicationWithGratefulShutdownEnabled();
        givenMessagesInTopic();
        whenStopApplication();
        thenAllMessagesAreProcessedOrKafkaIsShutdown();
    }

    @Test
    public void shouldNotWaitForMessagesWhenGratefulShutdownIsDisabled() {
        givenApplicationWithGratefulShutdownDisabled();
        givenMessagesInTopic();
        whenStopApplication();
        thenAllMessagesAreNotProcessed();
    }

    private void givenApplicationWithGratefulShutdownEnabled() {
        app.overrideConfigKey(GRATEFUL_SHUTDOWN_PROPERTY, Boolean.TRUE.toString());
        restart();
    }

    private void givenApplicationWithGratefulShutdownDisabled() {
        app.overrideConfigKey(GRATEFUL_SHUTDOWN_PROPERTY, Boolean.FALSE.toString());
        restart();
    }

    private void givenMessagesInTopic() {
        post("/slow-topic/sendMessages/" + TOTAL_MESSAGES);
    }

    private void whenStopApplication() {
        app.stop();
    }

    private void thenAllMessagesAreProcessedOrKafkaIsShutdown() {
        thenAssertLogs(line -> line.contains(LAST_MESSAGE_LOG) || line.contains(SHUTDOWN_KAFKA_LOG),
                "Expected output was not found in logs");
    }

    private void thenAllMessagesAreNotProcessed() {
        thenLogsDoNotContain(LAST_MESSAGE_LOG);
        thenLogsDoNotContain(SHUTDOWN_KAFKA_LOG);
    }

    private void thenLogsDoNotContain(String expectedOutput) {
        thenAssertLogs(line -> !line.contains(expectedOutput), "Unexpected output was found in logs");
    }

    private void thenAssertLogs(Predicate<String> assertion, String message) {
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            List<String> elements = Collections.emptyList();
            try {
                elements = Files.readAllLines(logfile);
            } catch (IOException ignored) {

            }

            assertTrue(elements.stream().anyMatch(assertion), message + ":" + elements);
        });
    }

    private void restart() {
        app.stop();
        app.start();
        if (logfile != null) {
            try {
                Files.write(logfile, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                // ignored
            }
        }
    }
}
