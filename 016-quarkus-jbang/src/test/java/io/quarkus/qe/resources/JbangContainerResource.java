package io.quarkus.qe.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class JbangContainerResource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer jbangContainer;
    private static final int JBANG_APP_PORT = 9090;
    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static final String WORKING_DIRECTORY = "/ws";
    private static final String APP_FOLDER_STRUCTURE = "/src/main/java/";
    private String script;
    private Map<String, String> env = new HashMap<>();
    private Map<String, String> config;

    @Override
    public Map<String, String> start() {
        config = new HashMap<>();
        jbangContainer = new GenericContainer(DockerImageName.parse("quay.io/jbangdev/jbang-action"))
                .withFileSystemBind(PROJECT_DIR, WORKING_DIRECTORY, BindMode.READ_WRITE)
                .withWorkingDirectory(WORKING_DIRECTORY)
                .withCommand(WORKING_DIRECTORY  + APP_FOLDER_STRUCTURE + script)
                .withEnv(env)
                .withExposedPorts(JBANG_APP_PORT);

        jbangContainer
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(
                        Wait.forLogMessage(".*Listening on.*", 1).withStartupTimeout(Duration.ofMinutes(5))
                ).start();

        config.put("jbang.app.port", String.valueOf(jbangContainer.getFirstMappedPort()));
        config.put("jbang.app.host", jbangContainer.getHost());
        return config;
    }

    @Override
    public void stop() {
        if (Objects.nonNull(jbangContainer)) jbangContainer.stop();
    }

    public String getHost(){
        return config.get("jbang.app.host");
    }

    public int getPort(){
        return Integer.valueOf(config.get("jbang.app.port"));
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

}