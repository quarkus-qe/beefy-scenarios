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

    private static final int CHILD_SIZE = 1;
    private static final int CHILD_BATCH_SIZE = 30;

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = CHILD_SIZE)
    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "someEntity")
    @BatchSize(size = CHILD_BATCH_SIZE)
    private Set<SomeChildEntity> someChildren;

    public void addSomeChild(SomeChildEntity child) {
        if (someChildren == null) {
            this.someChildren = new HashSet<>();
        }

        this.someChildren.add(child);
        child.setSomeEntity(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SomeChildEntity> getSomeChildren() {
        return someChildren;
    }

    public void setSomeChildren(Set<SomeChildEntity> someChildren) {
        this.someChildren = someChildren;
    }
}
