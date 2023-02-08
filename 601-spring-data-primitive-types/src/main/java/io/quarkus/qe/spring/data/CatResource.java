package io.quarkus.qe.spring.data;

import java.util.List;

import io.quarkus.qe.spring.data.model.Cat;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path("/cat")
public class CatResource {

    private final CatRepository catRepository;

    public CatResource(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @GET
    @Path("/customFindDistinctivePrimitive/{id}")
    @Produces("text/plain")
    public Boolean customFindDistinctivePrimitive(@PathParam("id") Long id) {
        return catRepository.customFindDistinctivePrimitive(id);
    }

    @GET
    @Path("/customFindDistinctiveObject/{id}")
    @Produces("text/plain")
    public Boolean customFindDistinctiveObject(@PathParam("id") Long id) {
        return catRepository.customFindDistinctiveObject(id);
    }

    @GET
    @Path("/findCatsByMappedSuperclassField/{deathReason}")
    @Produces("application/json")
    public List<Cat> findCatsByMappedSuperclassField(@PathParam("deathReason") String deathReason) {
        return catRepository.findCatsByDeathReason(deathReason);
    }
}
