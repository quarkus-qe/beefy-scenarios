package io.quarkus.qe.kafka.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class LoginAttempt {
    public String id;
    public String endpoint;
    public int code;

    public LoginAttempt(String id, String endpoint, int code) {
        this.id = id;
        this.endpoint = endpoint;
        this.code = code;
    }

    public LoginAttempt() {
    }
}