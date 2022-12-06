package io.quarkus.qe.someext.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class SomeExtensionProcessor {

    private static final String FEATURE = "some-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerManyClassesForReflection(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        for (int i = 0; i < 30_000; i++) {
            reflectiveClass.produce(new ReflectiveClassBuildItem(false, true, String.format("io.quarkus.qe.SomeClass%d", i)));
        }
    }
}
