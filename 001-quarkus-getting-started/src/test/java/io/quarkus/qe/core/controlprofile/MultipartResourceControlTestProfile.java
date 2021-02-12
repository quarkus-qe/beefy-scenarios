package io.quarkus.qe.core.controlprofile;

import io.quarkus.test.junit.QuarkusTestProfile;

public class MultipartResourceControlTestProfile implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "control";
    }
}
