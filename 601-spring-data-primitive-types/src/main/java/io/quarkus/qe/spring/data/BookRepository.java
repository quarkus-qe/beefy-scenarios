package io.quarkus.qe.spring.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.quarkus.qe.spring.data.model.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/9192
    @Query(value = "SELECT b.publicationYear FROM Book b where b.bid = :bid")
    int customFindPublicationYearPrimitive(@Param("bid") Integer bid);

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/9192
    @Query(value = "SELECT b.publicationYear FROM Book b where b.bid = :bid")
    Integer customFindPublicationYearObject(@Param("bid") Integer bid);

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/9192
    @Query(value = "SELECT b.isbn FROM Book b where b.bid = :bid")
    long customFindPublicationIsbnPrimitive(@Param("bid") Integer bid);

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/9192
    @Query(value = "SELECT b.isbn FROM Book b where b.bid = :bid")
    Long customFindPublicationIsbnObject(@Param("bid") Integer bid);

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/13015
    List<Book> findByPublisherAddressZipCode(@Param("zipCode") String zipCode);
}
