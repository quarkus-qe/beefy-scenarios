package io.quarkus.qe.quartz;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/scheduler/count")
public class CountResource {

    @Inject
    AnnotationScheduledCounter annotationScheduledcounter;

    @Inject
    ManuallyScheduledCounter manuallyScheduledCounter;

    @GET
    @Path("annotation")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getAnnotationCount() {
        return annotationScheduledcounter.get();
    }

    @GET
    @Path("manual")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getManualCount() {
        return manuallyScheduledCounter.get();
    }
}
