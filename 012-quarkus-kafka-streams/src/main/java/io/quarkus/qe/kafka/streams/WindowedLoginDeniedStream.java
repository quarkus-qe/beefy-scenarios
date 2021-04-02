package io.quarkus.qe.kafka.streams;

import io.quarkus.kafka.client.serialization.JsonbSerde;
import io.quarkus.qe.kafka.model.LoginAggregation;
import io.quarkus.qe.kafka.model.LoginAttempt;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.time.Duration;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@ApplicationScoped
public class WindowedLoginDeniedStream {

    static final String LOGIN_AGGREGATION_STORE = "login-aggregation-store";
    static final String LOGIN_ATTEMPTS_TOPIC = "login-http-response-values";
    static final String LOGIN_DENIED_AGGREGATED_TOPIC = "login-denied";

    @ConfigProperty(name = "login.denied.windows.sec")
    int WINDOWS_LOGIN_SEC;

    @ConfigProperty(name = "login.denied.threshold")
    int THRESHOLD;

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        JsonbSerde<LoginAttempt> loginAttemptSerde = new JsonbSerde<>(LoginAttempt.class);
        JsonbSerde<LoginAggregation> loginAggregationSerde = new JsonbSerde<>(LoginAggregation.class);

        builder.stream(LOGIN_ATTEMPTS_TOPIC, Consumed.with(Serdes.String(), loginAttemptSerde))
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofSeconds(WINDOWS_LOGIN_SEC)))
                .aggregate(LoginAggregation::new,
                        (id, value, aggregation) -> aggregation.updateFrom(value),
                        Materialized.<String, LoginAggregation, WindowStore<Bytes, byte[]>> as(LOGIN_AGGREGATION_STORE)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(loginAggregationSerde))
                .toStream()
                .filter((k, v) -> (v.code == UNAUTHORIZED.getStatusCode() || v.code == FORBIDDEN.getStatusCode()))
                .filter((k,v) -> v.count > THRESHOLD)
                .to(LOGIN_DENIED_AGGREGATED_TOPIC);

        return builder.build();
    }

    @Incoming(LOGIN_DENIED_AGGREGATED_TOPIC)
    @Outgoing("login-alerts")
    @Broadcast
    public String fanOut(String jsonLoginAggregation) {
        return jsonLoginAggregation;
    }
}
