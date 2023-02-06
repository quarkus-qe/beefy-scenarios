package io.quarkus.qe.hibernate.items;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Role {

    @Id
    public Long id;

    @Column
    public String name;

}