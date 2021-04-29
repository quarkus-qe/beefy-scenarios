package io.quarkus.qe.hibernate.items;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

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