package io.quarkus.qe.spring.data;

import java.util.List;

import io.quarkus.qe.spring.data.model.Book;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path("/book")
public class BookResource {

    private final BookRepository bookRepository;

    public BookResource(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PUT
    @Produces("application/json")
    @Path("/{id}")
    public Book updateBook(@PathParam("id") Integer id, final Book book) {
        verifyBookExists(id);
        book.setBid(id);
        return bookRepository.save(book);
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

    @GET
    @Path("/publisher/zipcode/{zipCode}")
    @Produces("application/json")
    public List<Book> findBooksByZipCode(@PathParam("zipCode") String zipCode) {
        return bookRepository.findByPublisherAddressZipCode(zipCode);
    }

    private void verifyBookExists(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException(String.format("book with id=%d was not found", id));
        }
    }
}
