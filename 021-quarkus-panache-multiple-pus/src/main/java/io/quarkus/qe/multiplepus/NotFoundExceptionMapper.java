package io.quarkus.qe.multiplepus;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

// workaround for Quarkus providing its own NotFoundExceptionMapper
// which is more specific than our ApplicationExceptionMapper
// see https://github.com/quarkusio/quarkus/issues/15272
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return new ApplicationExceptionMapper().toResponse(exception);
    }
}
