package io.quarkus.qe;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name = "application")
public class ApplicationEntity extends PanacheEntity {
    @NotEmpty
    @Column(unique = true, nullable = false)
    public String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "version_id", nullable = false)
    public VersionEntity version;
}
