package io.quarkus.qe.vertx.web.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Person extends Record {
    private String name;
    private String lastName;
    private long born;
    private long died;
    private int iq;
    private double lat;
    private double lon;
    private String[] geneticsModifications;

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

    public long getBorn() {
        return born;
    }

    public void setBorn(long born) {
        this.born = born;
    }

    public long getDied() {
        return died;
    }

    public void setDied(long died) {
        this.died = died;
    }

    public int getIq() {
        return iq;
    }

    public void setIq(int iq) {
        this.iq = iq;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String[] getGeneticsModifications() {
        return geneticsModifications;
    }

    public void setGeneticsModifications(String[] geneticsModifications) {
        this.geneticsModifications = geneticsModifications;
    }
}
