package io.quarkus.qe;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/application/filterBy")
@Transactional
public class ApplicationQueryResource {
    @GET
    @Path("/{filterName}")
    @Produces("application/json")
    public List<ApplicationEntity> filterBy(@PathParam("filterName") String filterName, @Context UriInfo uriInfo) {
        PanacheQuery<ApplicationEntity> query = ApplicationEntity.<ApplicationEntity> findAll()
                .filter(filterName, asMap(uriInfo.getQueryParameters()));
        return query.list();
    }

    private Map<String, Object> asMap(MultivaluedMap<String, String> map) {
        if (map == null) {
            return Collections.emptyMap();
        }

        return map.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey,
                        e -> e.getValue().stream().collect(Collectors.joining(","))));
    }
}
