package io.quarkus.qe;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jwt.JWK;
import io.vertx.ext.jwt.JWT;
import org.junit.jupiter.api.Test;

public class JwtTest {
    //  openssl genrsa -out private.key 2048
    //  openssl req -new -x509 -key private.key -out publickey.cer -days 3650

    public static final String CERTIFICATE_ONE = "MIIDZTCCAk2gAwIBAgIUT5swGuyNT9aUDFItflkCJQ2BdVYwDQYJKoZIhvcNAQEL\n" +
            "BQAwQjELMAkGA1UEBhMCWFgxFTATBgNVBAcMDERlZmF1bHQgQ2l0eTEcMBoGA1UE\n" +
            "CgwTRGVmYXVsdCBDb21wYW55IEx0ZDAeFw0yMDEyMjIwOTU2MjNaFw0zMDEyMjAw\n" +
            "OTU2MjNaMEIxCzAJBgNVBAYTAlhYMRUwEwYDVQQHDAxEZWZhdWx0IENpdHkxHDAa\n" +
            "BgNVBAoME0RlZmF1bHQgQ29tcGFueSBMdGQwggEiMA0GCSqGSIb3DQEBAQUAA4IB\n" +
            "DwAwggEKAoIBAQC7Jr43XlRWTyuigsKp+aVJYsSL6RlrhSKa+wPp2VWL5rVSn8/8\n" +
            "V+u2onFxuwEus3BEBGH+lF0u/3Phxs+q0/0bwxrDMfL52kgV3lVblgwWQW/pZaRO\n" +
            "Ng1gr+4uOoZyzeoWaufdGu9jfSEFkuuzRXbzt2sQbiUcNFlEHMf/+tyD0hP/4n0x\n" +
            "9P45fbnzJG0Puq/Vso2Bl9N6a6oz204PRoxxhVLCATaElGWYI4V4FdD8Gz+yTmae\n" +
            "QRQsUo+MEztxgAcQQZFbqklca0/tMIc8WdhIk9lBtbYyx99qthY/wfOa3OkiWcpK\n" +
            "1hxCITZ1GPzINkLf7Dvqi2YrApRqNjLcRG/TAgMBAAGjUzBRMB0GA1UdDgQWBBT/\n" +
            "goykwIMlR7jEg7bIwoidrvG+SzAfBgNVHSMEGDAWgBT/goykwIMlR7jEg7bIwoid\n" +
            "rvG+SzAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQCJJj0Pa6Xi\n" +
            "M5B3PMZjx/3Bb5SVUqiklGTYxpql4w4l7v82EBFDB+NdtkILNbzjxZyfbdz17Bv8\n" +
            "uo4/BUCIgvmHKBuYR54Ie1rjEe2dSMIm0pYQBetOLNU0ukV1yJ+aawzv680KBVvz\n" +
            "eDs2m8EJ0BUysh+1JTWWI5nJ1mbjVaSVDylnNRvaN9cUGTBKvGQtXMTCracXHIg4\n" +
            "fmGug3T9GsswJorX+xn3O0jPpoYAouHfMhKkyx2lXdm/7oq2HlXc6QOylxZ4FUEr\n" +
            "pUJnaHA2ErjRa5kHvP5aqEvmSnKGfDB+8XvIUUDnl3R6QDGkzdTjlFHC4lQAITxq\n" +
            "7huYsdhAMap2";

    public static final String CERTIFICATE_TWO = "MIIDZTCCAk2gAwIBAgIUBD3+ujEHrvjX67xTgBUzSecAiRMwDQYJKoZIhvcNAQEL\n" +
            "BQAwQjELMAkGA1UEBhMCWFgxFTATBgNVBAcMDERlZmF1bHQgQ2l0eTEcMBoGA1UE\n" +
            "CgwTRGVmYXVsdCBDb21wYW55IEx0ZDAeFw0yMDEyMjIwOTU0NDNaFw0zMDEyMjAw\n" +
            "OTU0NDNaMEIxCzAJBgNVBAYTAlhYMRUwEwYDVQQHDAxEZWZhdWx0IENpdHkxHDAa\n" +
            "BgNVBAoME0RlZmF1bHQgQ29tcGFueSBMdGQwggEiMA0GCSqGSIb3DQEBAQUAA4IB\n" +
            "DwAwggEKAoIBAQC7Jr43XlRWTyuigsKp+aVJYsSL6RlrhSKa+wPp2VWL5rVSn8/8\n" +
            "V+u2onFxuwEus3BEBGH+lF0u/3Phxs+q0/0bwxrDMfL52kgV3lVblgwWQW/pZaRO\n" +
            "Ng1gr+4uOoZyzeoWaufdGu9jfSEFkuuzRXbzt2sQbiUcNFlEHMf/+tyD0hP/4n0x\n" +
            "9P45fbnzJG0Puq/Vso2Bl9N6a6oz204PRoxxhVLCATaElGWYI4V4FdD8Gz+yTmae\n" +
            "QRQsUo+MEztxgAcQQZFbqklca0/tMIc8WdhIk9lBtbYyx99qthY/wfOa3OkiWcpK\n" +
            "1hxCITZ1GPzINkLf7Dvqi2YrApRqNjLcRG/TAgMBAAGjUzBRMB0GA1UdDgQWBBT/\n" +
            "goykwIMlR7jEg7bIwoidrvG+SzAfBgNVHSMEGDAWgBT/goykwIMlR7jEg7bIwoid\n" +
            "rvG+SzAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQB+HnVw7C9c\n" +
            "qGSZ0qk8JO6k6y9/rAOJZmtL0v+ndpDwAowgZalnYjZQ+8K6N/2xz+g4o0EgBJIj\n" +
            "Nxicuo0kUoOwLolUIBpk2MnEQkQWAf/hTqWJ0K+pcUsjoXTDKrFbHh/gy9Gk24RS\n" +
            "VvUZnnDPM61W4vD6vIMCxC7aQ13SW2/IwOF9u3jRUFduLfEZvjBK+vtu+kPyzWCz\n" +
            "fmxP1Ow3QPZJfHaHj+aXddWZGOD5cP5karziVNZUYmEPqNrrs5MlSCPYGdFSp+wN\n" +
            "Vt8+8lZ2/YGppkV/88t1zYYQ6UxisQt4q/u25wJ+j9fFDDnYq7Mt/orCMrKODBe4\n" +
            "TNFkafBY6Jo/";

    @Test
    void x5cCertificateChainTest() {

        new JWT().addJWK(new JWK(new JsonObject()
                .put("kty", "RSA")
                .put("alg", "RS256")
                .put("x5c", new JsonArray()
                        .add(CERTIFICATE_ONE)
                        .add(CERTIFICATE_TWO))));

        //TODO Convert from String to Resource file
    }
}