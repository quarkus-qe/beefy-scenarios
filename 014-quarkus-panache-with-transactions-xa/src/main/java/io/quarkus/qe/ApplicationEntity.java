package io.quarkus.qe;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "application")
public class ApplicationEntity extends PanacheEntity {
    @Column(unique = true, nullable = false)
    public String name;
}
