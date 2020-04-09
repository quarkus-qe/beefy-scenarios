package io.quarkus.qe.hibernate.validator;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;

@Entity
public class SomeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 1)
    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "someEntity")
    @BatchSize(size = 30)
    private Set<SomeChildEntity> someChilds;

    public void addSomeChild(SomeChildEntity child) {
        if (someChilds == null) {
            this.someChilds = new HashSet<>();
        }

        this.someChilds.add(child);
        child.setSomeEntity(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SomeChildEntity> getSomeChilds() {
        return someChilds;
    }

    public void setSomeChilds(Set<SomeChildEntity> someChilds) {
        this.someChilds = someChilds;
    }
}
