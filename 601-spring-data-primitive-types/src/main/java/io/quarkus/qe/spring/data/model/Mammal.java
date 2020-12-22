package io.quarkus.qe.spring.data.model;

import javax.persistence.MappedSuperclass;

// issue QUARKUS-525
@MappedSuperclass
public class Mammal extends Animal {

}
