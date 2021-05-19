package io.quarkus.qe.configmapping;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "person")
public class Person {
    String name;
    int age;
}
