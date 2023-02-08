package io.quarkus.qe.ping.exceptions;

import static jakarta.ws.rs.core.Response.status;

import org.jboss.logging.Logger;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOG = Logger.getLogger(CustomExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException e) {
        LOG.info("Custom exception mapper invoked.");

        if (e instanceof NotFoundException) {
            return status(Status.NOT_FOUND)
                    .entity(toCatalogError(new UnexpectedException(e.getMessage())))
                    .build();
        }

        return status(Status.INTERNAL_SERVER_ERROR)
                .entity(toCatalogError(new UnexpectedException(e.getMessage())))
                .build();
    }

    private CustomError toCatalogError(CustomException e) {
        return CustomError.builder().withCode(e.getErrorCode()).withMsg(e.getMessage()).build();
    }
}
