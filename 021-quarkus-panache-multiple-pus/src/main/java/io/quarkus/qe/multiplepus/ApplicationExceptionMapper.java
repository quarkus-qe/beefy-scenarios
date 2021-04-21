package io.quarkus.qe.multiplepus;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        int code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        if (exception instanceof WebApplicationException) {
            code = ((WebApplicationException) exception).getResponse().getStatus();
        }

        return Response.status(code)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ObjectMapper().createObjectNode()
                        .put("code", code)
                        .put("error", exception.getMessage())
                        .toString()
                )
                .build();
    }
}
