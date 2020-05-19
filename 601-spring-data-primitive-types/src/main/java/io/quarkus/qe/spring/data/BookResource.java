package io.quarkus.qe.spring.data;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/book")
public class BookResource {

    private final BookRepository bookRepository;

    public BookResource(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GET
    @Path("/customPublicationYearPrimitive/{bid}")
    @Produces("text/plain")
    public Integer customFindPublicationYearPrimitive(@PathParam("bid") Integer bid) {
        return bookRepository.customFindPublicationYearPrimitive(bid);
    }

    @GET
    @Path("/customPublicationYearObject/{bid}")
    @Produces("text/plain")
    public Integer customFindPublicationYearObject(@PathParam("bid") Integer bid) {
        return bookRepository.customFindPublicationYearObject(bid);
    }

    @GET
    @Path("/customPublicationIsbnPrimitive/{bid}")
    @Produces("text/plain")
    public Long customFindPublicationIsbnPrimitive(@PathParam("bid") Integer bid) {
        return bookRepository.customFindPublicationIsbnPrimitive(bid);
    }

    @GET
    @Path("/customPublicationIsbnObject/{bid}")
    @Produces("text/plain")
    public Long customFindPublicationIsbnObject(@PathParam("bid") Integer bid) {
        return bookRepository.customFindPublicationIsbnObject(bid);
    }

}
