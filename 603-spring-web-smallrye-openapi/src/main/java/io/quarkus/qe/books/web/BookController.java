package io.quarkus.qe.books.web;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.quarkus.qe.books.persistence.model.Book;
import io.quarkus.qe.books.persistence.repo.BookRepository;
import io.quarkus.qe.books.web.exception.BookIdMismatchException;
import io.quarkus.qe.books.web.exception.BookNotFoundException;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public Iterable<Book> findAll() {
        return bookRepository.listAll();
    }

    @GetMapping("/title/{bookTitle}")
    public List<Book> findByTitle(@PathVariable String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable long id) {
        return bookRepository.findByIdOptional(id)
                .orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Book create(@RequestBody Book book) {
        bookRepository.persist(book);
        return book;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable long id) {
        bookRepository.findByIdOptional(id)
                .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public Book updateBook(@RequestBody Book book, @PathVariable long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        final Book savedBook = bookRepository.findByIdOptional(id)
                .orElseThrow(BookNotFoundException::new);
        savedBook.setAuthor(book.getAuthor());
        savedBook.setTitle(book.getTitle());
        return book;
    }
}
