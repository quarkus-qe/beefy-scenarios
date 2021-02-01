package io.quarkus.qe.vertx.sql.test.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import static io.quarkus.qe.vertx.sql.test.resources.Db2TestProfile.PROFILE;

public class Db2Resource implements QuarkusTestResourceLifecycleManager {

    GenericContainer db2Container;

    @Override
    public Map<String, String> start() {
        Map<String, String> config = new HashMap<>();
        String profile = System.getProperty("quarkus.test.profile");
        if(profile.equals(PROFILE)) defaultDb2Container(config);

        return config;
    }

    private void defaultDb2Container(Map<String, String> config) {
        db2Container = new GenericContainer(DockerImageName.parse("quay.io/pjgg/db2:11.5.5.0"))
                .withPrivilegedMode(true)
                .withEnv("LICENSE", "accept")
                .withEnv("DB2INST1_PASSWORD", "test")
                .withEnv("DB2INSTANCE", "test")
                .withEnv("AUTOCONFIG", "false")
                .withEnv("ARCHIVE_LOGS", "false")
                .withEnv("DBNAME", "amadeus")
                .withExposedPorts(50000);

        db2Container.waitingFor(new HostPortWaitStrategy()).waitingFor(
                Wait.forLogMessage(".*Setup has completed\\..*", 1).withStartupTimeout(Duration.ofMinutes(10))
        ).start();

        config.put("quarkus.datasource.db2.jdbc.url", String.format("jdbc:db2://%s:%d/amadeus", db2Container.getHost(), db2Container.getFirstMappedPort()));
        config.put("quarkus.datasource.db2.reactive.url", String.format("db2://%s:%d/amadeus", db2Container.getHost(), db2Container.getFirstMappedPort()));
        config.put("app.selected.db","db2");
        config.put("quarkus.flyway.migrate-at-start","false");
        config.put("quarkus.flyway.mysql.migrate-at-start","false");
    }

    @Override
    public void stop() {
        if (Objects.nonNull(db2Container)) db2Container.stop();
    }

}
