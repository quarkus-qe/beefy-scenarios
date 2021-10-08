package io.quarkus.qe.bulk;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@Disabled("Can't inject beans annotated with @ConfigProperties. Reported in https://github.com/quarkusio/quarkus/issues/20610")
public class NativeBulkOfPropertiesIT extends BulkOfPropertiesTest {

}
