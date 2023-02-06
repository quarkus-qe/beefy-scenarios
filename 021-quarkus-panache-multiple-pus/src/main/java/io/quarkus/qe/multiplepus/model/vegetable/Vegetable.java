package io.quarkus.qe.multiplepus.model.vegetable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vegetable")
public class Vegetable extends PanacheEntity {

    @NotBlank(message = "Vegetable name must be set!")
    public String name;

}
