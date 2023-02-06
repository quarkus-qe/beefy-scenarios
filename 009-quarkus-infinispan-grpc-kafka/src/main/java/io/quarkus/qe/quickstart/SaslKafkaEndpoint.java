package io.quarkus.qe.quickstart;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import io.quarkus.runtime.StartupEvent;

@Path("/kafka/sasl")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class SaslKafkaEndpoint extends KafkaEndpoint {

    @Inject
    @Named("kafka-consumer-sasl")
    KafkaConsumer<String, String> saslConsumer;

    @Inject
    @Named("kafka-producer-sasl")
    KafkaProducer<String, String> saslProducer;

    @Inject
    @Named("kafka-admin-sasl")
    AdminClient saslAdmin;

    public void initialize(@Observes StartupEvent ev) {
        super.initialize(saslConsumer);
    }

    @Path("/topics")
    @GET
    public Set<String> getTopics() throws InterruptedException, ExecutionException, TimeoutException {
        return super.getTopics(saslAdmin);
    }

    @POST
    public long produceEvent(@QueryParam("key") String key, @QueryParam("value") String value)
            throws InterruptedException, ExecutionException, TimeoutException {
        return super.produceEvent(saslProducer, key, value);
    }

    @GET
    public String getLast() {
        return super.getLast();
    }
}
