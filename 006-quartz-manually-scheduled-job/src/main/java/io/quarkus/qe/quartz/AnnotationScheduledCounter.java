package io.quarkus.qe.quartz;

import io.quarkus.qe.quartz.services.CounterService;
import io.quarkus.scheduler.Scheduled;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AnnotationScheduledCounter {

    @Inject
    CounterService service;

    @PostConstruct
    void init() {
        service.reset(caller());
    }

    public int get() {
        return service.get(caller());
    }

    @Scheduled(cron = "0/1 * * * * ?")
    void increment() {
        service.invoke(caller());
    }

    private static final String caller() {
        return AnnotationScheduledCounter.class.getName();
    }

}
