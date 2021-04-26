package io.quarkus.qe.hibernate.items;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Account {

    @Id
    public Long id;

    @Column(length = 255, unique = true, nullable = false)
    @NotNull
    @Size(max = 255)
    public String email;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdOn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_in_role", joinColumns = @JoinColumn(name = "accountid"), inverseJoinColumns = @JoinColumn(name = "roleid"))
    public Set<Role> roles = new HashSet<>();

}
