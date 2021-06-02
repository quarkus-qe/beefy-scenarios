package io.quarkus.qe.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "antagonist")
public interface AntagonistConfiguration {
    String message();

    String name();

    AntagonistWifeConfig wife();

    interface AntagonistWifeConfig {
        String name();

        String message();
    }
}
