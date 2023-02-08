package io.quarkus.qe.hibernate.validator;

import com.sun.istack.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Entity
public class Hello {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String greetingText;

    @Valid
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Human greetedHuman;

    @Valid
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private SomeEntity someEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Human getGreetedHuman() {
        return greetedHuman;
    }

    public void setGreetedHuman(Human greetedHuman) {
        this.greetedHuman = greetedHuman;
    }

    public String getGreetingText() {
        return greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }

    public SomeEntity getSomeEntity() {
        return someEntity;
    }

    public void setSomeEntity(SomeEntity someEntity) {
        this.someEntity = someEntity;
    }

}
