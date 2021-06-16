package io.quarkus.qe.kafka.shutdown;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/slow-topic")
public class SlowTopicResource {

    @Inject
    @Channel("slow-topic")
    Emitter<String> emitter;

    @POST
    @Path("/sendMessages/{count}")
    public void sendMessage(@PathParam("count") Integer count) {
        for (int index = 1; index <= count; index++) {
            emitter.send("Message " + index);
        }
    }
}
