package io.quarkus.qe.spring.data;

import io.quarkus.test.common.TestResourceScope;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

@WithTestResource(value = H2DatabaseTestResource.class, scope = TestResourceScope.MATCHING_RESOURCES)
public class TestResources {
}
