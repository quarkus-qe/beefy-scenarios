package io.quarkus.qe.books;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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