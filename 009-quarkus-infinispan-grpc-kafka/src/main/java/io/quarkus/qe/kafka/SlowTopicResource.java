package io.quarkus.qe.kafka;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.runtime.ShutdownEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/slow-topic")
public class SlowTopicResource {

    @ConfigProperty(name = "mp.messaging.outgoing.slow-topic.topic")
    public String slowTopic;

    @Inject
    KafkaProducer<String, String> producer;

    public void terminate(@Observes ShutdownEvent ev) {
        producer.close();
    }

    @POST
    @Path("/sendMessages/{count}")
    public void sendMessage(@PathParam("count") Integer count) throws Exception {
        for (int index = 1; index <= count; index++) {
            String eventID = UUID.randomUUID().toString();
            ProducerRecord record = new ProducerRecord<>(slowTopic, eventID, "Message " + index);
            producer.send(record).get(1, TimeUnit.SECONDS);
        }
    }
}
