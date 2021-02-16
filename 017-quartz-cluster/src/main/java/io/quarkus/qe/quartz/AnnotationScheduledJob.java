package io.quarkus.qe.quartz;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class AnnotationScheduledJob {

    @ConfigProperty(name = "owner.name")
    String ownerName;

    @Inject
    ExecutionService service;

    @Transactional
    @Scheduled(cron = "0/1 * * * * ?", identity = "my-unique-task")
    void increment() {
        service.addExecution(ownerName);
    }

}