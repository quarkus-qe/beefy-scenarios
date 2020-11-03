package org.acme.quickstart;

import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/infinispan")
public class GreetingResource {

    @Inject
    @Remote("mycache")
    RemoteCache<String, String> cache;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return cache.get("hello");
    }
}