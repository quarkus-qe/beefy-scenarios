package io.quarkus.qe.vertx.kafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class StockPriceProcessedTest implements KafkaEmitterUtils {

    protected static List<StockPrice> received = new CopyOnWriteArrayList<>();

    @Test
    @DisplayName("Vert.x Kafka [flavor: mutiny] -> All incoming events must be completed")
    public void producerConsumesAvroTest() {
        await().atMost(15000, MILLISECONDS).until(() -> received.size() == 3);
        received.forEach(item -> assertTrue(item.getStatus() == status.COMPLETED));
    }
}
