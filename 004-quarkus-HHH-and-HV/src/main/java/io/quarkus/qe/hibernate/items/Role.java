package io.quarkus.qe.hibernate.items;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

    @Id
    public Long id;

    @Column
    public String name;

}