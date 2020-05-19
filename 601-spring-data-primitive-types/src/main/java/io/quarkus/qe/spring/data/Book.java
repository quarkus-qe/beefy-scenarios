package io.quarkus.qe.spring.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book extends NamedEntity {

    private Integer bid;

    private Integer publicationYear;

    private Long isbn;

    public Book() {
    }

    public Book(Integer bid, String name, Integer publicationYear) {
        super(name);
        this.bid = bid;
        this.publicationYear = publicationYear;
    }

    @Id
    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }
}
