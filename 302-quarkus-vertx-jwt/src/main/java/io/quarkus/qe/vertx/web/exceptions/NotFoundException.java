package io.quarkus.qe.vertx.web.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public int getHttpErrorCode() {
        return HttpResponseStatus.NOT_FOUND.code();
    }
}
