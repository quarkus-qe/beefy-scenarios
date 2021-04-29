package io.quarkus.qe.multiplepus.model.vegetable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "vegetable")
public class Vegetable extends PanacheEntity {

    @NotBlank(message = "Vegetable name must be set!")
    public String name;

}
