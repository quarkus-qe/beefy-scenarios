package io.quarkus.qe.vertx.kafka;

import io.quarkus.qe.vertx.kafka.config.VertxKProducerConfig;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.Vertx;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class VerticleDeployer {

    @Inject
    VertxKProducerConfig config;

    @Inject
    KStockPriceProducer producer;

    public void run(@Observes StartupEvent e, Vertx vertx, Instance<AbstractVerticle> verticles) {
        long delayMs = TimeUnit.SECONDS.toMillis(config.delay);
        vertx.setPeriodic(delayMs, c -> producer.generate().subscribe());

        // Deploy all abstract verticles example.
        for (AbstractVerticle verticle : verticles) {
            vertx.deployVerticle(verticle).await().indefinitely();
        }
    }

}
