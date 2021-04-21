package io.quarkus.qe.core;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "known_fruits")
@NamedQuery(name = "Fruits.findAll",
      query = "SELECT f FROM Fruit f ORDER BY f.name",
      hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class Fruit {

    private static final int FRUIT_ID_INITIAL_VALUE = 10;
    private static final int FRUIT_NAME_MAX_LENGTH = 40;

    @Id
    @SequenceGenerator(
            name = "fruitsSequence",
            sequenceName = "known_fruits_id_seq",
            allocationSize = 1,
            initialValue = FRUIT_ID_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fruitsSequence")
    private Integer id;

    @Column(length = FRUIT_NAME_MAX_LENGTH, unique = true)
    private String name;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}