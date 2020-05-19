package io.quarkus.qe.quartz;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
