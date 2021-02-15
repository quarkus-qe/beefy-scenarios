package io.quarkus.qe.quartz.resources;

import java.util.Collections;

import org.junit.jupiter.api.extension.ExtensionContext;

import io.quarkus.qe.quartz.AnnotationScheduledJob;
import io.quarkus.qe.quartz.ExecutionEntity;
import io.quarkus.qe.quartz.ExecutionService;

public class QuartzNodeApplicationResource extends ApplicationResource {

    public static final String OWNER_NAME = "owner.name";

    public QuartzNodeApplicationResource(String ownerName) {
        super(classes(AnnotationScheduledJob.class, ExecutionEntity.class, ExecutionService.class),
                Collections.singletonMap(OWNER_NAME, ownerName));
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        try {
            super.afterAll(extensionContext);
        } catch (Exception ex) {
            // ignore all
        }

    }

}
