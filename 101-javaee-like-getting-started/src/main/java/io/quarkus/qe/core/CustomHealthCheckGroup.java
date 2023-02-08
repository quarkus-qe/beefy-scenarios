package io.quarkus.qe.core;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import io.smallrye.health.api.HealthGroup;

import jakarta.enterprise.context.ApplicationScoped;

@HealthGroup("customGroup")
@ApplicationScoped
public class CustomHealthCheckGroup implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.builder().name("custom-group").up().build();
    }
}
