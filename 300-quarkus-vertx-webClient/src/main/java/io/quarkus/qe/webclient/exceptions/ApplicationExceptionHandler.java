package io.quarkus.qe.webclient.exceptions;

import io.smallrye.mutiny.TimeoutException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {

        Response error = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

        if(e instanceof TimeoutException)
            error = Response.status(Response.Status.REQUEST_TIMEOUT).entity(e.getMessage()).build();

        return error;
    }
}
