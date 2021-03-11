package io.quarkus.qe.quartz;

import static io.quarkus.qe.quartz.resources.ApplicationResource.NATIVE;
import static io.quarkus.qe.quartz.resources.ApplicationResource.QUARKUS_PROFILE;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = QUARKUS_PROFILE, matches = NATIVE)
public class NativeAnnotationScheduledJobsQuartzIT extends AnnotationScheduledJobsQuartzTest {
}
