package io.quarkus.qe.hibernate.validator;

import io.quarkus.qe.hibernate.resources.CustomH2DatabaseTestResource;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(CustomH2DatabaseTestResource.class)
public class TestResources {
}
