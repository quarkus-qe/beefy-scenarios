package io.quarkus.qe.multiplepus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.netty.handler.codec.http.HttpResponseStatus;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final int ERROR_CODE = HttpResponseStatus.UNPROCESSABLE_ENTITY.code();

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode errors = mapper.createArrayNode();

        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            errors.addObject()
                    .put("path", constraintViolation.getPropertyPath().toString())
                    .put("message", constraintViolation.getMessage());
        }

        return Response.status(ERROR_CODE)
                .type(MediaType.APPLICATION_JSON)
                .entity(mapper.createObjectNode()
                        .put("code", ERROR_CODE)
                        .set("error", errors)
                )
                .build();
    }
}
