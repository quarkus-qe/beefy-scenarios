package io.quarkus.qe.multiplepus.model.fruit;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "fruit")
public class Fruit extends PanacheEntity {

    @NotBlank(message = "Fruit name must be set!")
    public String name;

}
