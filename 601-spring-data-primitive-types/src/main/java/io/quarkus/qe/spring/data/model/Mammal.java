package io.quarkus.qe.spring.data.model;

import javax.persistence.MappedSuperclass;

//This is for regression test for https://github.com/quarkusio/quarkus/pull/13015
@MappedSuperclass
public class Mammal extends Animal {

}
