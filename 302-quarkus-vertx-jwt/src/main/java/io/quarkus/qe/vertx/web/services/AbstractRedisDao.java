package io.quarkus.qe.vertx.web.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import io.quarkus.qe.vertx.web.exceptions.NotFoundException;
import io.quarkus.qe.vertx.web.model.Record;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;

public abstract class AbstractRedisDao<E extends Record> {

    @Inject
    ReactiveRedisClient redisClient;

    Class<E> type;

    String prefix;

    public AbstractRedisDao(String prefix, Class<E> type) {
        this.prefix = prefix;
        this.type = type;
    }

    public Uni<Boolean> upsert(E entity) {
        return redisClient.set(Arrays.asList(buildKey(entity.getId()), entity.toJsonEncoded()))
                .onItem().transform(this::isPersisted);
    }

    public Uni<E> get(String key) {
        return redisClient.get(buildKey(key))
                .onItem().ifNull().failWith(new NotFoundException("No item: " + key))
                .onItem().ifNotNull().transform(item -> Record.decodeJSON(item.toString(), type));
    }

    public Uni<List<E>> get() {
        Multi<E> objects = getKeys().onItem().transformToUniAndMerge(
                key -> redisClient.get(key).onItem().ifNotNull().transform(item -> Record.decodeJSON(item.toString(), type)));
        return objects.collect().in(ArrayList::new, List::add);
    }

    public Uni<Boolean> delete(String... key) {
        return redisClient.del(buildKey(key)).onItem().transform(this::isDeleted);
    }

    private String buildKey(String key) {
        return prefix + key;
    }

    private List<String> buildKey(String... keys) {
        return Stream.of(keys).map(this::buildKey).collect(Collectors.toList());
    }

    private Multi<String> getKeys() {
        return redisClient.keys(buildKey("*"))
                .onItem().transformToMulti(array -> Multi.createFrom().iterable(getKeysValues(array)))
                .onItem().castTo(String.class);
    }

    private boolean isDeleted(Response resp) {
        if (!"0".equalsIgnoreCase(resp.toString()))
            return true;
        else
            throw new NotFoundException("Redis entity not deleted.");
    }

    private boolean isPersisted(Response resp) {
        if ("OK".equalsIgnoreCase(resp.toString()))
            return true;
        else
            throw new RuntimeException("Redis entity not persisted.");
    }

    private Set<String> getKeysValues(Response response) {
        final Set<String> keys = new HashSet<>();
        for (int i = 0; i < response.size(); i++) {
            keys.add(response.get(i).toString());
        }
        return keys;
    }
}
