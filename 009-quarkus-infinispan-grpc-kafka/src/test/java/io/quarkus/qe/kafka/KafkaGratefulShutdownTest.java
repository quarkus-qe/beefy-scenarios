package io.quarkus.qe.kafka;

import static io.restassured.RestAssured.post;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern JAVA_LISTEN_PID = Pattern.compile("(LISTEN)(\\s{0,10})([0-9]{0,6}/java)");
    private static final String APP_PORT = "8083";

    @RegisterExtension
    static QuarkusProdModeTest app = new QuarkusProdModeTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .addClasses(SlowTopicResource.class, SlowTopicConsumer.class, KafkaProviders.class))
            .setRuntimeProperties(Collections.singletonMap("quarkus.http.port", APP_PORT))
            .overrideConfigKey(GRATEFUL_SHUTDOWN_PROPERTY, Boolean.TRUE.toString())
            .setLogFileName("out.log")
            .overrideConfigKey(KAFKA_LOG_PROPERTY, Level.DEBUG.getName())
            .setRun(true);

    @LogFile
    Path logfile;

    @Test
    public void shouldWaitForMessagesWhenGratefulShutdownIsEnabled() throws IOException, InterruptedException {
        givenMessagesInTopic();
        whenStopApplication();
        thenAllMessagesAreProcessedOrKafkaIsShutdown();
    }

    private void givenMessagesInTopic() {
        post("/slow-topic/sendMessages/" + TOTAL_MESSAGES);
    }

    private void whenStopApplication() throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec("kill -s SIGTERM " + getAppPID());
        logProcessOutput(proc);
        proc.waitFor();
    }

    private String getAppPID() throws InterruptedException, IOException {
        String[] cmd = { "/bin/sh", "-c", "netstat -anop | grep " + APP_PORT };
        Process pidProc = Runtime.getRuntime().exec(cmd);
        String stdout;
        InputStreamReader isr = new InputStreamReader(pidProc.getInputStream());
        BufferedReader rdr = new BufferedReader(isr);
        Set<String> matches = new HashSet<>();
        while ((stdout = rdr.readLine()) != null) {
            Matcher m = JAVA_LISTEN_PID.matcher(stdout);
            while (m.find())
                matches.add(m.group(3));
        }
        pidProc.waitFor();
        assertThat("Application PID not found", matches.size(), is(1));

        return matches.iterator().next().replace("/java", "");
    }

    private void logProcessOutput(Process proc) throws IOException {
        String line;
        InputStreamReader isr = new InputStreamReader(proc.getInputStream());
        BufferedReader rdr = new BufferedReader(isr);
        while ((line = rdr.readLine()) != null) {
            System.out.println(line);
        }

        isr = new InputStreamReader(proc.getErrorStream());
        rdr = new BufferedReader(isr);
        while ((line = rdr.readLine()) != null) {
            System.out.println(line);
        }
    }

    private void thenAllMessagesAreProcessedOrKafkaIsShutdown() {
        thenAssertLogs(line -> line.contains(LAST_MESSAGE_LOG) || line.contains(SHUTDOWN_KAFKA_LOG),
                "Expected output was not found in logs");
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
}
