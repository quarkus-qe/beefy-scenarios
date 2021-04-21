package io.quarkus.qe.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "antagonist")
public class AntagonistConfiguration {
    public String message;
    public String name;
    public AntagonistWifeConfig wife;

    public static class AntagonistWifeConfig {
        public String name;
        public String message;
    }
}
