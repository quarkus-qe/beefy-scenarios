package io.quarkus.qe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SqlFragmentAlias;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity(name = "application")
@FilterDef(name = "useLikeByName", parameters = { @ParamDef(name = "name", type = "string") })
@Filter(name = "useLikeByName", condition = "name like '%' || :name || '%'")
@FilterDef(name = "useServiceByName", parameters = { @ParamDef(name = "name", type = "string") })
public class ApplicationEntity extends PanacheEntity {
    @NotEmpty(message = "name can't be null")
    @Column(unique = true, nullable = false)
    public String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "version_id", nullable = false)
    public VersionEntity version;

    @JsonManagedReference
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Filter(name = "useServiceByName", condition = "{s}.name = :name", aliases = {
            @SqlFragmentAlias(alias = "s", table = "service")
    })
    public List<ServiceEntity> services = new ArrayList<>();

    @Transient
    public Optional<ServiceEntity> getServiceByName(String serviceName) {
        return services.stream().filter(s -> serviceName.equals(s.name)).findFirst();
    }
}
