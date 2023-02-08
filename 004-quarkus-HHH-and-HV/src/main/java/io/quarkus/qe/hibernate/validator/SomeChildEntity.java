package io.quarkus.qe.hibernate.validator;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class SomeChildEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SomeEntity someEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SomeEntity getSomeEntity() {
        return someEntity;
    }

    public void setSomeEntity(SomeEntity someEntity) {
        this.someEntity = someEntity;
    }
}
