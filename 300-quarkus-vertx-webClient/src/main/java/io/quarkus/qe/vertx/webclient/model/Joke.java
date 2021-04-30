package io.quarkus.qe.vertx.webclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonIgnoreProperties(ignoreUnknown = true)
@RegisterForReflection
public class Joke {

    @JsonProperty("id")
    private String id;

    @JsonProperty("value")
    private String jokeText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Joke() {
    }

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }

    @Override
    public String toString() {
        return String.format("[ID: %s jokeText: %s]", id, jokeText);
    }
}
