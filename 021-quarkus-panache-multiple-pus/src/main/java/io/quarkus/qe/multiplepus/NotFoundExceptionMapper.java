package io.quarkus.qe.multiplepus;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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
