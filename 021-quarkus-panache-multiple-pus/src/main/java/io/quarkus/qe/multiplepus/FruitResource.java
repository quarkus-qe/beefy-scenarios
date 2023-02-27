package io.quarkus.qe.multiplepus;

import java.util.List;

import io.quarkus.panache.common.Sort;
import io.quarkus.qe.multiplepus.model.fruit.Fruit;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("fruit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FruitResource {

    @GET
    public List<Fruit> getAll() {
        return Fruit.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Fruit get(@PathParam("id") Long id) {
        Fruit fruit = Fruit.findById(id);
        if (fruit == null) {
            throw new NotFoundException("fruit '" + id + "' not found");
        }
        return fruit;
    }

    @POST
    @Transactional
    public Response create(@Valid Fruit fruit) {
        if (fruit.id != null) {
            throw new ClientErrorException("unexpected ID in request", 422);
        }

        System.out.println(" => " + fruit.id);
        fruit.persist();
        System.out.println(" => " + fruit.id);
        return Response.ok(fruit).status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Fruit update(@PathParam("id") Long id, @Valid Fruit newFruit) {
        Fruit fruit = Fruit.findById(id);
        if (fruit == null) {
            throw new NotFoundException("fruit '" + id + "' not found");
        }

        fruit.name = newFruit.name;
        return fruit;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Fruit fruit = Fruit.findById(id);
        if (fruit == null) {
            throw new NotFoundException("fruit '" + id + "' not found");
        }
        fruit.delete();
        return Response.status(204).build();
    }

}
