package io.quarkus.qe.vertx.web.model;

import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@RegisterForReflection
public abstract class Record {

    private String id;
    private long createdAt;
    private long updatedAt;

    public Record() {
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();
        id = UUID.randomUUID().toString();
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

    public String toJsonEncoded() {
        return toJson().encode();
    }

    public static <E> E decodeJSON(String json, Class<E> type) {
        return Json.decodeValue(json, type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
