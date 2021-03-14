package io.quarkus.qe.multiplepus;

import io.quarkus.panache.common.Sort;
import io.quarkus.qe.multiplepus.model.vegetable.Vegetable;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("vegetable")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VegetableResource {

    @GET
    public List<Vegetable> getAll() {
        return Vegetable.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Vegetable get(@PathParam("id") Long id) {
        Vegetable vegetable = Vegetable.findById(id);
        if (vegetable == null) {
            throw new NotFoundException("vegetable '" + id + "' not found");
        }
        return vegetable;
    }

    @POST
    @Transactional
    public Response create(@Valid Vegetable vegetable) {
        if (vegetable.id != null) {
            throw new ClientErrorException("unexpected ID in request", 422);
        }

        vegetable.persist();
        return Response.ok(vegetable).status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Vegetable update(@PathParam("id") Long id, @Valid Vegetable newVegetable) {
        Vegetable vegetable = Vegetable.findById(id);
        if (vegetable == null) {
            throw new NotFoundException("vegetable '" + id + "' not found");
        }

        vegetable.name = newVegetable.name;
        return vegetable;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Vegetable vegetable = Vegetable.findById(id);
        if (vegetable == null) {
            throw new NotFoundException("vegetable '" + id + "' not found");
        }
        vegetable.delete();
        return Response.status(204).build();
    }

}
