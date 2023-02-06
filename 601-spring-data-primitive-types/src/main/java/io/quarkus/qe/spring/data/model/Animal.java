package io.quarkus.qe.spring.data.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

//This is for regression test for https://github.com/quarkusio/quarkus/pull/13015
@MappedSuperclass
public class Animal {

    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime birthDay;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime died;

    private String deathReason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    public LocalDateTime getDied() {
        return died;
    }

    public void setDied(LocalDateTime died) {
        this.died = died;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }
}
