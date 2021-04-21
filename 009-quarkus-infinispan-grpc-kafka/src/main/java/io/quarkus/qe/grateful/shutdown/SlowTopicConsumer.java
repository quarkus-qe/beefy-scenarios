package io.quarkus.qe.grateful.shutdown;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SlowTopicConsumer {

    public static final int LOOP_TIMEOUT = 100;

    private static final Logger LOG = Logger.getLogger(SlowTopicConsumer.class);

    @Incoming("slow")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<Void> process(Message<String> message) throws InterruptedException {
        Thread.sleep(LOOP_TIMEOUT);
        LOG.info("Processed " + message.getPayload());
        return message.ack();
    }
}