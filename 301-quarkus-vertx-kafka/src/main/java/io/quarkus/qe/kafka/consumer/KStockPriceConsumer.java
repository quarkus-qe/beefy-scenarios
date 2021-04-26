package io.quarkus.qe.kafka.consumer;

import io.quarkus.qe.kafka.StockPrice;
import io.quarkus.qe.kafka.status;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import java.util.function.BiConsumer;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
    public String process(StockPrice next){
        next.setStatus(status.COMPLETED);
        LOG.infov("CONSUMER -> ID: {0}, PRICE: {1}", next.getId(), next.getPrice());
        emitter.send(next).whenComplete(handlerEmitterResponse(KStockPriceConsumer.class.getName()));
        return next.getId() + "-" + next.getPrice() + "-" + next.getStatus();
    }

    private BiConsumer<Void, Throwable> handlerEmitterResponse(final String owner) {
        return (success, failure) -> {
            if (failure != null) {
                LOG.info(String.format("D'oh! %s", failure.getMessage()));
            }
        };
    }
}
