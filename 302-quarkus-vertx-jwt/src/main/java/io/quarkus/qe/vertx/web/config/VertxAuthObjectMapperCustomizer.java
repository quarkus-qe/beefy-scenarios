package io.quarkus.qe.vertx.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkus.jackson.ObjectMapperCustomizer;

import jakarta.inject.Singleton;

@Singleton
public class VertxAuthObjectMapperCustomizer implements ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
