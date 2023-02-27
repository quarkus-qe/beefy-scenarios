package io.quarkus.qe.multiplepus.model.fruit;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "fruit")
public class Fruit extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "fruitsSequence", sequenceName = "rs_fruit_sequence", allocationSize = 1, initialValue = 1)
    //    @SequenceGenerator(name = "fruitsSequence", sequenceName = "fruits_id_seq", allocationSize = 1, initialValue = 8)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fruitsSequence")
    public Integer id;

    @NotBlank(message = "Fruit name must be set!")
    public String name;

}
