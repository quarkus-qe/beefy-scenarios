package io.quarkus.qe.spring.data.configuration;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import jakarta.enterprise.inject.spi.CDI;

@Configuration
public class AppConfiguration {

    private AtomicInteger counter = new AtomicInteger(0);

    @Bean
    @Scope(scopeName = "prototype")
    public CounterBean produceCounterBean() {
        return new CounterBean(counter.getAndIncrement());
    }

    @Bean
    @Scope(scopeName = "singleton")
    public InstanceIdBean produceInstanceIdBean() {
        return new InstanceIdBean(UUID.randomUUID().toString());
    }

    @Bean
    @Scope(scopeName = "request")
    public RequestIdBean produceRequestIdBean(InstanceIdBean instanceId) {
        return new RequestIdBean(UUID.randomUUID().toString(), instanceId.getInstanceId());
    }

    @Bean
    @Scope(scopeName = "session")
    public SessionIdBean sessionIdBean(InstanceIdBean instanceId) {
        return new SessionIdBean(UUID.randomUUID().toString(), instanceId.getInstanceId());
    }

    public static int getAndIncIndex() {
        CounterBean counter = CDI.current().select(CounterBean.class).get();
        return counter.getAmount();
    }
}
