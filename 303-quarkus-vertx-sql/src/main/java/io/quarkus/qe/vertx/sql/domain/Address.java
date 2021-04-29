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

@Schema(name = "Address", description = "Address entity")
@RegisterForReflection
public class Address extends Record {
    private String street;
    private String blockNumber;
    private String zipCode;
    private String city;
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Uni<Long> save(DbPoolService sqlClient) {
        List<String> fieldsNames = Arrays.asList("street,block_number,zip_code,city,country,created_at".split(","));
        List<Object> fieldsValues = Stream
                .of(getStreet(), getBlockNumber(), getZipCode(), getCity(), getCountry(), getCreatedAt())
                .collect(Collectors.toList());
        return sqlClient.save("address", fieldsNames, fieldsValues);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return street.equals(address.street) &&
                blockNumber.equals(address.blockNumber) &&
                zipCode.equals(address.zipCode) &&
                city.equals(address.city) &&
                country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, blockNumber, zipCode, city, country);
    }
}
