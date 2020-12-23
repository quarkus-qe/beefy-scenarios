package io.quarkus.qe;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name = "application")
public class ApplicationEntity extends PanacheEntity {
    @Column(unique = true, nullable = false)
    public String name;
}
