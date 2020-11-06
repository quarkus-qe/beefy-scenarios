package io.quarkus.qe.vertx.kafka;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KStockPriceConsumer extends AbstractVerticle implements KafkaEmitterUtils {

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
    public StockPrice process(StockPrice next){
        next.setStatus(status.COMPLETED);

        emitter.send(next).whenComplete(handlerEmitterResponse(KStockPriceConsumer.class.getName()));
        return next;
    }
}
