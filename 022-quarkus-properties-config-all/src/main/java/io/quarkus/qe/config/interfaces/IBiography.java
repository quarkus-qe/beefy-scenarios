package io.quarkus.qe.config.interfaces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface IBiography {

    @ConfigProperty(name = "hobby")
    String hobby();
}
