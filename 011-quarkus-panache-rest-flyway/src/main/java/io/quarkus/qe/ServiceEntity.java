package io.quarkus.qe;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "service")
public class ServiceEntity extends PanacheEntity {
    @Column(nullable = false)
    public String name;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    @JsonBackReference
    public ApplicationEntity application;
}
