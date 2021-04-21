package io.quarkus.qe.quartz;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import io.quarkus.qe.quartz.services.CounterService;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Startup
@ApplicationScoped
public class ManuallyScheduledCounter {
    @Inject
    org.quartz.Scheduler quartz;

    @Inject
    CounterService service;

    public int get() {
        return service.get(caller());
    }

    @Transactional
    @PostConstruct
    void init() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(CountingJob.class).build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();
        quartz.scheduleJob(job, trigger);
    }

    @RegisterForReflection
    public static class CountingJob implements Job {
        @Inject
        CounterService service;

        @PostConstruct
        void init() {
            service.reset(caller());
        }

        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            service.invoke(caller());
        }
    }

    private static String caller() {
        return ManuallyScheduledCounter.class.getName();
    }
}
