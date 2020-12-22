package io.quarkus.qe.spring.data.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Book extends NamedEntity {
    @Id
    private Integer bid;

    private Integer publicationYear;

    private Long isbn;

    @ManyToOne(targetEntity=Address.class)
    @JoinColumn(name = "addressId")
    private Address publisherAddress;

    public Book() {
    }

    public Book(Integer bid, String name, Integer publicationYear) {
        super(name);
        this.bid = bid;
        this.publicationYear = publicationYear;
    }

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

    public Address getPublisherAddress() {
        return publisherAddress;
    }

    public void setPublisherAddress(Address publisherAddress) {
        this.publisherAddress = publisherAddress;
    }
}
