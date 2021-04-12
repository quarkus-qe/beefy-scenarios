package io.quarkus.qe.vertx.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import java.time.ZonedDateTime;
import java.util.Arrays;

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

    BladeRunner bladeRunner;
    Replicant replicant;
    Vertx vertx;

    @BeforeEach
    public void setup() {
        this.vertx = Vertx.vertx();
        bladeRunner = defaultBladeRunner();
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .body(bladeRunner.toJsonEncoded())
                .when()
                .post("/bladeRunner/")
                .then()
                .log().body()
                .statusCode(200);

        replicant = defaultReplicant();
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .body(replicant.toJsonEncoded())
                .when()
                .post("/replicant/")
                .then()
                .statusCode(200);
    }

    @AfterEach
    public void teardown(){
        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/bladeRunner/" + bladeRunner.getId())
                .then()
                .statusCode(anyOf(is(204),is(404)));

        given().accept(ContentType.JSON)
                .headers("Authorization", "Bearer " + JWT(Invalidity.EMPTY, "admin"))
                .when()
                .delete("/replicant/"+replicant.getId())
                .then()
                .statusCode(anyOf(is(204),is(404)));
    }

    protected enum Invalidity {
        EMPTY,
        WRONG_ISSUER,
        WRONG_AUDIENCE,
        AHEAD_OF_TIME,
        EXPIRED,
        WRONG_KEY
    }

    protected String JWT(Invalidity invalidity, String... groups) {
        JsonObject authConfig = defaultAuthConfig();
        JsonObject claims = defaultClaims(groups);
        JWTAuth jwt = JWTAuth.create(vertx.getDelegate(), new JWTAuthOptions()
                .addPubSecKey(getPubSecKeyOptions(authConfig)));
        switch(invalidity){
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

    private JsonObject defaultClaims(String... groups){
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
        return currentTime().toInstant().toEpochMilli() / 1000L;
    }

    private Long currentTimePLusOneEpoch(){
        return currentTime().plusMinutes(1).toInstant().toEpochMilli() / 1000L;
    }

    private ZonedDateTime currentTime() {
        return ZonedDateTime.now();
    }

    protected BladeRunner defaultBladeRunner(){
        BladeRunner bladeRunner = new BladeRunner();
        bladeRunner.setDailyRate(2000.00);
        bladeRunner.setRetirements(103);
        bladeRunner.setVoightKampffTestAmount(1500);
        bladeRunner.setName("Rick");
        bladeRunner.setLastName("Deckard");
        bladeRunner.setIq(105);

        return bladeRunner;
    }

    protected Replicant defaultReplicant(){
        Replicant replicant = new Replicant();
        replicant.setFugitive(true);
        replicant.setLiveSpanYears(5);
        replicant.setModel("Nexus 5");
        replicant.setTelepathy(true);
        replicant.setName("Alan");
        replicant.setLastName("Greg");
        replicant.setIq(185);

        return replicant;
    }
}
