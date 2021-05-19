package io.quarkus.qe.configmapping;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ConfigMappingResourceTest {

    private static final String EXPECTED_PERSON_NAME = "Sheldon";
    private static final int EXPECTED_PERSON_AGE = 18;
    private static final String EXPECTED_PERSON_LABEL_A = "Label 1";
    private static final String EXPECTED_PERSON_LABEL_B = "Label 2";
    private static final String EXPECTED_OVERRIDES_PERSON_NAME = "Karen";
    private static final int EXPECTED_OVERRIDES_PERSON_AGE = 23;

    @Test
    public void shouldInjectFieldWithConfigMapping() {
        assertResponseIs("/person/name/from-field", EXPECTED_PERSON_NAME);
        assertResponseIs("/person/age/from-field", EXPECTED_PERSON_AGE);
    }

    @Test
    public void shouldInjectFieldUsingOverriddenConfigWithConfigMapping() {
        assertResponseIs("/person/name/from-overrides-field", EXPECTED_OVERRIDES_PERSON_NAME);
        assertResponseIs("/person/age/from-overrides-field", EXPECTED_OVERRIDES_PERSON_AGE);
    }

    @Test
    public void shouldInjectInterfaceWithConfigMapping() {
        assertResponseIs("/person/name/from-interface", EXPECTED_PERSON_NAME);
        assertResponseIs("/person/age/from-interface", EXPECTED_PERSON_AGE);
        assertResponseIs("/person/label/A/from-interface", EXPECTED_PERSON_LABEL_A);
        assertResponseIs("/person/label/B/from-interface", EXPECTED_PERSON_LABEL_B);
    }

    private <T> void assertResponseIs(String path, T expected) {
        given().when().get("/config-mapping" + path)
                .then().statusCode(HttpStatus.SC_OK)
                .body(is(expected.toString()));
    }
}
