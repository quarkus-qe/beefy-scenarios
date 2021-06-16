package io.quarkus.qe.quickstart;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public abstract class KafkaEndpoint {

    public static final String TOPIC = "hello";
    volatile boolean done = false;
    volatile String last;

    protected void initialize(KafkaConsumer<String, String> consumer) {
        consumer.subscribe(Collections.singleton(TOPIC));
        new Thread(() -> {
            while (!done) {
                final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

                consumerRecords.forEach(record -> {
                    System.out.printf("Polled Record:(%s, %s, %d, %d)\n",
                            record.key(), record.value(),
                            record.partition(), record.offset());
                    last = record.key() + "-" + record.value();
                });
            }
            consumer.close();
        }).start();
    }

    protected Set<String> getTopics(AdminClient consumer) throws InterruptedException, ExecutionException, TimeoutException {
        return consumer.listTopics().names().get(5, TimeUnit.SECONDS);
    }

    protected long produceEvent(KafkaProducer<String, String> producer, String key, String value)
            throws InterruptedException, ExecutionException, TimeoutException {
        return producer.send(new ProducerRecord<>(TOPIC, key, value)).get(5, TimeUnit.SECONDS)
                .offset();
    }

    protected String getLast() {
        return last;
    }
}
