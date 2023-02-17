package io.quarkus.qe.bulk;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class NativeConfigValueIT extends ConfigValueTest {

    @Override
    protected boolean isNativeExecution() {
        return true;
    }
}
