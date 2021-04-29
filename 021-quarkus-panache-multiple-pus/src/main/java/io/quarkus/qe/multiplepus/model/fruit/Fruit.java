package io.quarkus.qe.multiplepus.model.fruit;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "fruit")
public class Fruit extends PanacheEntity {

    @NotBlank(message = "Fruit name must be set!")
    public String name;

}
