package io.quarkus.qe.vertx.jwt;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.impl.jose.JWK;
import io.vertx.ext.auth.impl.jose.JWT;

public class JwtTest {

    @Test
    void x5cCertificateChainTest() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File certOne = new File(classLoader.getResource("certs/pubCertOne.cer").getFile());
        File certTwo = new File(classLoader.getResource("certs/pubCertTwo.cer").getFile());

        new JWT().addJWK(new JWK(new JsonObject()
                .put("kty", "RSA")
                .put("alg", "RS256")
                .put("x5c", new JsonArray()
                        .add(new String(Files.readAllBytes(Paths.get(certOne.getPath()))))
                        .add(new String(Files.readAllBytes(Paths.get(certTwo.getPath())))))));
    }
}