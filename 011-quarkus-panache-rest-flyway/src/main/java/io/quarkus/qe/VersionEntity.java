package io.quarkus.qe;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "version")
public class VersionEntity extends PanacheEntityBase {
    @Id
    public String id;
}
