package io.quarkus.qe.vertx.sql.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlClientHelper;

@Schema(name = "Passenger", description = "Passenger entity")
@RegisterForReflection
public class Passenger extends Record {
    private String nif;
    private String name;
    private String lastName;
    private String contactNumber;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Uni<Long> save(DbPoolService sqlClient) {
        return SqlClientHelper.inTransactionUni(sqlClient, tx -> address.save(sqlClient).onItem().transformToUni(address_id -> {
            List<String> fieldsNames = Arrays.asList("nif,name,last_name,contact_number,created_at, address_id".split(","));
            List<Object> fieldsValues = Stream
                    .of(getNif(), getName(), getLastName(), getContactNumber(), getCreatedAt(), address_id)
                    .collect(Collectors.toList());
            return sqlClient.save("passenger", fieldsNames, fieldsValues);
        }));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Passenger passenger = (Passenger) o;
        return nif.equals(passenger.nif) &&
                name.equals(passenger.name) &&
                lastName.equals(passenger.lastName) &&
                contactNumber.equals(passenger.contactNumber) &&
                address.equals(passenger.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif, name, lastName, contactNumber, address);
    }
}
