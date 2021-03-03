package io.quarkus.qe.spring.data.model;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.persistence.AttributeConverter;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

class MapStringConverter implements AttributeConverter<Map<String, String>, String> {
    private static final String EMPTY = "{}";
    private final Logger logger = Logger.getLogger(MapStringConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (IOException e) {
            logger.error("Error while converting Map to JSON String: ", e);
            return "";
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(Optional.ofNullable(dbData).orElse(EMPTY), Map.class);
        } catch (Exception e) {
            logger.error("Error while converting JSON String to Map: ", e);
            return Collections.emptyMap();
        }
    }
}
