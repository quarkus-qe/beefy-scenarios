package io.quarkus.qe.quartz;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class ExecutionEntity extends PanacheEntity {
    public Long seconds;
    public String owner;

}
