package io.quarkus.qe.quartz;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.scheduler.Scheduled;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
