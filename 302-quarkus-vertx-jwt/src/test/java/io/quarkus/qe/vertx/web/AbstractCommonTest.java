package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import java.time.ZonedDateTime;
import java.util.Arrays;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.quarkus.qe.vertx.web.model.BladeRunner;
import io.quarkus.qe.vertx.web.model.Replicant;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.mutiny.core.Vertx;

public class AbstractCommonTest {

    private static final long SECOND_IN_MILLIS = 1000L;

    private static final double BLADE_RUNNER_DAILY_RATE_DEFAULT = 2000.00;
    private static final int BLADE_RUNNER_RETIREMENTS_DEFAULT = 103;
    private static final int BLADE_RUNNER_VOIGHT_KAMPFF_DEFAULT = 1500;
    private static final int BLADE_RUNNER_SEQ_ID = 105;

    private static final int REPLICANT_LIVE_SPAN_YEARS = 5;
    private static final int REPLICANT_SEQ_ID = 185;

    BladeRunner bladeRunner;
    Replicant replicant;
    Vertx vertx;

    @BeforeEach
    public void setup() {
        this.vertx = Vertx.vertx();
        bladeRunner = defaultBladeRunner();
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .body(bladeRunner.toJsonEncoded())
                .when()
                .post("/bladeRunner/")
                .then()
                .log().body()
                .statusCode(HttpStatus.SC_OK);

        replicant = defaultReplicant();
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .body(replicant.toJsonEncoded())
                .when()
                .post("/replicant/")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @AfterEach
    public void teardown() {
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_NO_CONTENT), is(HttpStatus.SC_NOT_FOUND)));

        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + generateToken(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/replicant/" + replicant.getId())
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_NO_CONTENT), is(HttpStatus.SC_NOT_FOUND)));
    }

    protected enum Invalidity {
        EMPTY,
        WRONG_ISSUER,
        WRONG_AUDIENCE,
        AHEAD_OF_TIME,
        EXPIRED,
        WRONG_KEY
    }

    protected String generateToken(Invalidity invalidity, String... groups) {
        JsonObject authConfig = defaultAuthConfig();
        JsonObject claims = defaultClaims(groups);
        JWTAuth jwt = JWTAuth.create(vertx.getDelegate(), new JWTAuthOptions()
                .addPubSecKey(getPubSecKeyOptions(authConfig)));
        switch (invalidity) {
            case WRONG_ISSUER:
                claims.put("iss", "invalid");
                break;
            case WRONG_AUDIENCE:
                claims.put("aud", "invalid");
                break;
            case AHEAD_OF_TIME:
                claims.put("iat", currentTimePLusOneEpoch());
                break;
            case EXPIRED:
                claims.put("exp", currentTimeEpoch());
                break;
            case WRONG_KEY:
                authConfig.put("publicKey", "invalid");
                jwt = JWTAuth.create(vertx.getDelegate(), new JWTAuthOptions()
                        .addPubSecKey(getPubSecKeyOptions(authConfig)));
                break;
            default:
                throw new IllegalStateException(
                        String.format("Unexpected value %s of type %s", invalidity.name(), Invalidity.class.getName()));
        }

        return jwt.generateToken(claims);
    }

    private JsonObject defaultAuthConfig() {
        return new JsonObject()
                .put("symmetric", true)
                .put("algorithm", ConfigProvider.getConfig().getValue("authN.alg", String.class))
                .put("publicKey", ConfigProvider.getConfig().getValue("authN.secret", String.class));
    }

    private PubSecKeyOptions getPubSecKeyOptions(JsonObject authConfig) {
        return new PubSecKeyOptions(authConfig).setBuffer(authConfig.getBuffer("publicKey"));
    }

    private JsonObject defaultClaims(String... groups) {
        Long now = currentTimeEpoch();
        Long expiration = currentTimePLusOneEpoch();
        return new JsonObject()
                .put("name", "John Doe")
                .put("sub", "bff")
                .put("iss", "vertxJWT@redhat.com")
                .put("aud", "third_party")
                .put("groups", Arrays.asList(groups))
                .put("iat", now)
                .put("exp", expiration);
    }

    private Long currentTimeEpoch() {
        return currentTime().toInstant().toEpochMilli() / SECOND_IN_MILLIS;
    }

    private Long currentTimePLusOneEpoch() {
        return currentTime().plusMinutes(1).toInstant().toEpochMilli() / SECOND_IN_MILLIS;
    }

    private ZonedDateTime currentTime() {
        return ZonedDateTime.now();
    }

    protected BladeRunner defaultBladeRunner() {
        BladeRunner bladeRunner = new BladeRunner();
        bladeRunner.setDailyRate(BLADE_RUNNER_DAILY_RATE_DEFAULT);
        bladeRunner.setRetirements(BLADE_RUNNER_RETIREMENTS_DEFAULT);
        bladeRunner.setVoightKampffTestAmount(BLADE_RUNNER_VOIGHT_KAMPFF_DEFAULT);
        bladeRunner.setName("Rick");
        bladeRunner.setLastName("Deckard");
        bladeRunner.setIq(BLADE_RUNNER_SEQ_ID);

        return bladeRunner;
    }

    protected Replicant defaultReplicant() {
        Replicant replicant = new Replicant();
        replicant.setFugitive(true);
        replicant.setLiveSpanYears(REPLICANT_LIVE_SPAN_YEARS);
        replicant.setModel("Nexus 5");
        replicant.setTelepathy(true);
        replicant.setName("Alan");
        replicant.setLastName("Greg");
        replicant.setIq(REPLICANT_SEQ_ID);

        return replicant;
    }
}
