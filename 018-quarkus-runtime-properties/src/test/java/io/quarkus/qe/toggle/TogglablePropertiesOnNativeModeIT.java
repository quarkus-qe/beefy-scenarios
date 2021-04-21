package io.quarkus.qe.toggle;

import java.util.Collections;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusProdModeTest;

public class TogglablePropertiesOnNativeModeIT extends BaseTogglablePropertiesTest {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    @RegisterExtension
    static final QuarkusProdModeTest APP = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(APPLICATION_PROPERTIES, APPLICATION_PROPERTIES))
            .setRun(true)
            .setBuildNative(true);

    @Override
    protected void whenChangeServiceAtRuntime(TogglableServices service, boolean enable) {
        APP.stop();
        APP.setRuntimeProperties(Collections.singletonMap(service.getToggleProperty(), "" + enable));
        APP.start();
    }
}
