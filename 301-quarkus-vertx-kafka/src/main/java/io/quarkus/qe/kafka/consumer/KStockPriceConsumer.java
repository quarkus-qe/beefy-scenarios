package io.quarkus.qe.kafka.consumer;

import java.util.function.BiConsumer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.quarkus.qe.kafka.StockPrice;
import io.quarkus.qe.kafka.status;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class KStockPriceConsumer extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(KStockPriceConsumer.class);

    @Inject
    @Channel("sink-stock-price")
    @OnOverflow(value = OnOverflow.Strategy.DROP)
    Emitter<StockPrice> emitter;

    @Override
    public Uni<Void> asyncStart() {
        LOG.info("Verticle KStockPriceConsumer Up! (does nothing)");
        return Uni.createFrom().voidItem();
    }

    @Incoming("channel-stock-price")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @Outgoing("stock-monitor")
    @Broadcast
    public String process(StockPrice next) {
        next.setStatus(status.COMPLETED);
        LOG.debugv("CONSUMER -> ID: {0}, PRICE: {1}", next.getId(), next.getPrice());
        emitter.send(next).whenComplete(handlerEmitterResponse());
        return next.getId() + "-" + next.getPrice() + "-" + next.getStatus();
    }

    private BiConsumer<Void, Throwable> handlerEmitterResponse() {
        return (success, failure) -> {
            if (failure != null) {
                LOG.debugv("D'oh! {0}", failure.getMessage());
            }
        };
    }
}
