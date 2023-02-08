package io.quarkus.qe.hibernate.validator;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.sun.istack.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;

@Entity
public class Human {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "greetedHuman")
    @BatchSize(size = 30)
    private Set<Hello> greetings;

    public void addGreeting(Hello hello) {
        if (greetings == null) {
            greetings = new HashSet<>();
        }

        greetings.add(hello);
        hello.setGreetedHuman(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Hello> getGreetings() {
        return greetings;
    }

    public void setGreetings(Set<Hello> greetings) {
        this.greetings = greetings;
    }

}
