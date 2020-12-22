package io.quarkus.qe.spring.data;

import io.quarkus.qe.spring.data.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends Repository<Book, Integer> {

    // issue 9192
    @Query(value = "SELECT b.publicationYear FROM Book b where b.bid = :bid")
    int customFindPublicationYearPrimitive(@Param("bid") Integer bid);

    // issue 9192
    @Query(value = "SELECT b.publicationYear FROM Book b where b.bid = :bid")
    Integer customFindPublicationYearObject(@Param("bid") Integer bid);

    // issue 9192
    @Query(value = "SELECT b.isbn FROM Book b where b.bid = :bid")
    long customFindPublicationIsbnPrimitive(@Param("bid") Integer bid);

    // issue 9192
    @Query(value = "SELECT b.isbn FROM Book b where b.bid = :bid")
    Long customFindPublicationIsbnObject(@Param("bid") Integer bid);

    // issue QUARKUS-525
    List<Book> findByPublisherAddressZipCode(@Param("zipCode") String zipCode);
}
