package io.quarkus.qe.vertx.kafka;

import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface KafkaEmitterUtils {

    Logger LOG = Logger.getLogger(KafkaEmitterUtils.class);

    @NotNull
    default BiConsumer<Void, Throwable> handlerEmitterResponse(final String owner) {
        return (success, failure) -> {
            if (failure != null) {
                LOG.info(String.format("D'oh! %s", failure.getMessage()));
            } else {
                LOG.info(String.format("Message sent successfully. Owner %s", owner));
            }
        };
    }

}
