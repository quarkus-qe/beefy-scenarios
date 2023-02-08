package io.quarkus.qe.hibernate.items;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

@Entity
public class Customer {

    @Id
    public Long id;

    @Version
    @Column(name = "version")
    public int version;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdOn;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE })
    public Account account;

}