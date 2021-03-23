package io.quarkus.qe.openshift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

public class OpenShiftYamlHealthProbesIT {

    @Test
    public void testOpenShiftYamlHealthProbesWithCustomImageName() throws FileNotFoundException {
        String path = "target/kubernetes/kubernetes.yml";
        File file = new File(path);
        assertTrue(file.exists(), "File " + path + " doesn't exist");

        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(new File(path));
        boolean deploymentDefinitionFound = false;
        for (Object object : yaml.loadAll(inputStream)) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (map.get("kind").equals("Deployment")) {
                deploymentDefinitionFound = true;

                Map<String, Object> xmap = (Map<String, Object>) map.get("spec");
                Map<String, Object> ymap = (Map<String, Object>) xmap.get("template");
                Map<String, Object> zmap = (Map<String, Object>) ymap.get("spec");
                Map<String, Object> finalMap = (Map<String, Object>) ((List) zmap.get("containers")).get(0);

                // finalMap.entrySet().forEach(System.out::println);

                assertEquals("demo", finalMap.get("name"), "Deployment name is not expected 'demo'");
                assertTrue(finalMap.containsKey("livenessProbe"), "livenessProbe is not defined");
                assertTrue(finalMap.containsKey("readinessProbe"), "readinessProbe is not defined");
            }
        }
        if (!deploymentDefinitionFound) {
            fail("Parsed yaml file didn't have expected Deployment definition");
        }
    }
}
