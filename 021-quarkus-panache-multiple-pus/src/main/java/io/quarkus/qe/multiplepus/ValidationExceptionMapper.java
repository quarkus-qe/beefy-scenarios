package io.quarkus.qe.multiplepus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode errors = mapper.createArrayNode();

        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            errors.addObject()
                    .put("path", constraintViolation.getPropertyPath().toString())
                    .put("message", constraintViolation.getMessage());
        }

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(mapper.createObjectNode()
                        .put("code", 422)
                        .set("error", errors)
                )
                .build();
    }
}
