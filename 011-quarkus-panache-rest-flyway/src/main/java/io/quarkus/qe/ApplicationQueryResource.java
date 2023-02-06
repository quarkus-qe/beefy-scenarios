package io.quarkus.qe;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;

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
