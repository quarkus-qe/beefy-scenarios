package io.quarkus.qe.vertx.kafka;

import io.quarkus.qe.vertx.kafka.config.VertxKProducerConfig;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Random;
import java.util.stream.IntStream;

@ApplicationScoped
public class KStockPriceProducer implements KafkaEmitterUtils{

    @Inject
    VertxKProducerConfig config;

    @Inject
    @Channel("source-stock-price")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    Emitter<StockPrice> emitter;

    private Random random = new Random();

    public Uni<Void> generate() {
        IntStream.range(0, config.batchSize).forEach(next -> {
                StockPrice event = StockPrice.newBuilder().setId("IBM").setPrice(random.nextDouble()).setStatus(status.PENDING).build();
                emitter.send(event).whenComplete(handlerEmitterResponse(KStockPriceProducer.class.getName()));
        });

        return Uni.createFrom().voidItem();
    }



}
