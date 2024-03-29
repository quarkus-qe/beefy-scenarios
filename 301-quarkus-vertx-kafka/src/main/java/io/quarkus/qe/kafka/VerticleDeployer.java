package io.quarkus.qe.kafka;

import io.quarkus.qe.kafka.config.VertxKProducerConfig;
import io.quarkus.qe.kafka.producers.StockPriceProducer;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.Vertx;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class VerticleDeployer {

    @Inject
    VertxKProducerConfig config;

    @Inject
    StockPriceProducer producer;

    public void run(@Observes StartupEvent e, Vertx vertx, Instance<AbstractVerticle> verticles) {
        vertx.setPeriodic(config.delay(), c -> producer.generate().subscribe());

        // Deploy all abstract verticles example.
        for (AbstractVerticle verticle : verticles) {
            vertx.deployVerticle(verticle).await().indefinitely();
        }
    }

}
