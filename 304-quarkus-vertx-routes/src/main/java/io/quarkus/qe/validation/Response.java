package io.quarkus.qe.validation;

import io.quarkus.qe.validation.annotations.Uppercase;

import jakarta.validation.constraints.NotNull;

public class Response {
    @NotNull(message = "id can't be null")
    private String id;

    @Uppercase
    private String custom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
