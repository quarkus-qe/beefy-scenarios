package org.acme.spring.data.rest;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 2, max = 50, message = "length must be between {min} and {max}")
    @NotBlank(message = "Name may not be blank")
    private String name;

    @Length(min = 2, max = 50, message = "length must be between {min} and {max}")
    @NotBlank(message = "Author may not be blank")
    private String author;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;

    public Book() {
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}
