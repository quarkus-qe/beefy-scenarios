package io.quarkus.qe.quickstart;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
