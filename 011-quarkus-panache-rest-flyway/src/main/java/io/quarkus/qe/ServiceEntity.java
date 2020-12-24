package io.quarkus.qe;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name = "service")
public class ServiceEntity extends PanacheEntity {
    @Column(nullable = false)
    public String name;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    @JsonBackReference
    public ApplicationEntity application;
}
