package io.quarkus.qe.toggle;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;

public class TogglablePropertiesOnDevModeTest extends BaseTogglablePropertiesTest {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    @RegisterExtension
    static final QuarkusDevModeTest APP = new QuarkusDevModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(APPLICATION_PROPERTIES, APPLICATION_PROPERTIES));

    @Override
    protected void whenChangeServiceAtRuntime(TogglableServices service, boolean enable) {
        APP.modifyResourceFile(APPLICATION_PROPERTIES,
                s -> s.replace(service.getToggleProperty() + "=" + !enable, service.getToggleProperty() + "=" + enable));
    }
}
