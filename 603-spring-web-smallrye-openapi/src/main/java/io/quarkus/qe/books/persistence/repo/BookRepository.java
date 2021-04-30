package io.quarkus.qe.books.persistence.repo;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.qe.books.persistence.model.Book;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
    public List<Book> findByTitle(String title) {
        return list("title", title);
    }
}
