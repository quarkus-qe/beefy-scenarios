package io.quarkus.qe.multiplepus;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.panache.common.Sort;
import io.quarkus.qe.multiplepus.model.fruit.Fruit;

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
            throw new ClientErrorException("unexpected ID in request", HttpResponseStatus.UNPROCESSABLE_ENTITY.code());
        }

        fruit.persist();
        return Response.ok(fruit).status(Response.Status.CREATED).build();
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
        return Response.noContent().build();
    }

}
