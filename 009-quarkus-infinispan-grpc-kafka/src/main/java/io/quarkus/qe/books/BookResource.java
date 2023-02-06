package io.quarkus.qe.books;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;

@Path("/book")
public class BookResource {

    @Inject
    @Remote(BookCacheInitializer.CACHE_NAME)
    RemoteCache<String, Book> cache;

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBook(@PathParam("title") String title) {
        Book found = cache.get(title);
        return found;
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Book addBook(@Valid Book book) {
        return cache.put(book.getTitle(), book);
    }
}