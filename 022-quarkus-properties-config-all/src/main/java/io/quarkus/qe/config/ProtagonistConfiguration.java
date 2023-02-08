package io.quarkus.qe.config;

import io.quarkus.qe.config.interfaces.ProtagonistConfigurable;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ProtagonistConfiguration {

    @Inject
    ProtagonistConfigurable protagonistConfigurable;

    public String getName() {
        return protagonistConfigurable.name();
    }

    public String getMessage() {
        return protagonistConfigurable.message();
    }

    public String getHobby() {
        return protagonistConfigurable.hobby();
    }

    public String getFriendName() {
        return protagonistConfigurable.friend().name();
    }

    public String getFriendMessage() {
        return protagonistConfigurable.friend().message();
    }
}
