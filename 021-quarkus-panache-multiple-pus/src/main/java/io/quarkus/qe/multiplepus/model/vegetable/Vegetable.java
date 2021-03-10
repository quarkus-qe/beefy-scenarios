package io.quarkus.qe.multiplepus.model.vegetable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "vegetable")
public class Vegetable extends PanacheEntity {

    @NotBlank(message = "Vegetable name must be set!")
    public String name;

}
