package io.quarkus.qe.bulk;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@Disabled("TODO: Can't add configsource.properties because of https://github.com/quarkusio/quarkus/issues/17653")
@NativeImageTest
public class NativeBulkOfPropertiesIT extends BulkOfPropertiesTest {

}
