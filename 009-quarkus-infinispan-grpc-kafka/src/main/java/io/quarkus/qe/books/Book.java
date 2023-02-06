package io.quarkus.qe.books;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class Book {

    private String title;
    private String description;
    private int publicationYear;

    public Book() {

    }

    @ProtoFactory
    public Book(String title, String description, int publicationYear) {
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
        this.publicationYear = publicationYear;
    }

    @NotBlank(message = "Title cannot be blank")
    @ProtoField(number = 1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ProtoField(number = 2)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ProtoField(number = 3, defaultValue = "-1")
    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear &&
                title.equals(book.title) &&
                description.equals(book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, publicationYear);
    }
}
