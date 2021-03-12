package io.quarkus.qe.hibernate.items;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Item {

    @Id
    public Long id;

    public String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    public Customer customer;

}
