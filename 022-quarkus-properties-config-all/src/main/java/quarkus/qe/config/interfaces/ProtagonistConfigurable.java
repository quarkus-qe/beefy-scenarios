package quarkus.qe.config.interfaces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "protagonist")
public interface ProtagonistConfigurable extends IBiography {

    @ConfigProperty(name = "name")
    String name();

    @ConfigProperty(name = "message")
    String message();

    Friend friend();

    interface Friend {
        @ConfigProperty(name = "name")
        String name();

        @ConfigProperty(name = "message")
        String message();
    }
}
