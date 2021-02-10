package io.quarkus.qe.quartz;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.qe.quartz.services.CounterService;
import io.quarkus.scheduler.Scheduled;

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