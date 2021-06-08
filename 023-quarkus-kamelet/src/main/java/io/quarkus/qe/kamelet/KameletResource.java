package io.quarkus.qe.kamelet;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.FluentProducerTemplate;

@Path("/kamelet")
public class KameletResource {

    @Inject
    FluentProducerTemplate fluentProducerTemplate;

    @Inject
    ConsumerTemplate consumerTemplate;

    @Path("/produce")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String produceToKamelet(String message) {
        return fluentProducerTemplate.toF("kamelet:setBody/test?bodyValue=%s", message).request(String.class);
    }

    @Path("/timer")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Integer consumeFromKamelet() {
        return consumerTemplate.receiveBody("kamelet:tick", 10000, Integer.class);
    }

    @Path("/property")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String bodyFromApplicationProperty() {
        return fluentProducerTemplate.to("kamelet:setBodyFromProperties").request(String.class);
    }

    @Path("/chain")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String kameletChain(String message) {
        return fluentProducerTemplate.to("direct:chain").withBody(message).request(String.class);
    }
}
