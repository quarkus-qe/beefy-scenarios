package io.quarkus.qe.vertx.sql.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.Json;

@RegisterForReflection
public class Record {

    protected static final String QUALIFIED_ID = "id";
    private Long id;
    @JsonIgnore
    private long createdAt;
    @JsonIgnore
    private long updatedAt;

    public Record() {
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();
    }

    public Record(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String toJsonStringify() {
        return Json.encode(this);
    }

    public static String toJsonStringify(List<? extends Record> records) {
        return Json.encode(records);
    }
}
