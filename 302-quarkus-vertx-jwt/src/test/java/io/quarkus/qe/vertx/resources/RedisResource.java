package io.quarkus.qe.vertx.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisResource implements QuarkusTestResourceLifecycleManager {

    GenericContainer redisContainer;

    @Override
    public Map<String, String> start() {

        redisContainer = new GenericContainer(DockerImageName.parse("quay.io/bitnami/redis:6.0"))
                .withEnv("ALLOW_EMPTY_PASSWORD", "yes").withExposedPorts(6379);
        redisContainer.start();

        String redisConPath = String.format("redis://%s:%d", redisContainer.getHost(), redisContainer.getFirstMappedPort());

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.redis.hosts", redisConPath);

        return config;
    }

    @Override
    public void stop() {
        if (Objects.nonNull(redisContainer)) redisContainer.stop();
    }
}
