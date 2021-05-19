package io.quarkus.qe.vertx.web.exceptions;

import javax.enterprise.context.ApplicationScoped;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.UnauthorizedException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.HttpException;

@ApplicationScoped
public class FailureHandler {

    public void handler(final RoutingContext ctx) {
        JsonObject error = defaultError(ctx.normalisedPath());

        if (ctx.failure() instanceof NotFoundException) {
            NotFoundException notFoundExp = (NotFoundException) ctx.failure();
            error.put("status", notFoundExp.getHttpErrorCode())
                    .put("error", HttpResponseStatus.valueOf(notFoundExp.getHttpErrorCode()).reasonPhrase());

            ctx.response().setStatusCode(notFoundExp.getHttpErrorCode());
        }

        if (ctx.failure() instanceof HttpException) {
            HttpException httpExp = (HttpException) ctx.failure();
            error.put("status", httpExp.getStatusCode());
        }

        if (ctx.failure() instanceof UnauthorizedException) {
            error.put("status", HttpResponseStatus.UNAUTHORIZED.code());
            error.put("error", HttpResponseStatus.valueOf(HttpResponseStatus.UNAUTHORIZED.code()).reasonPhrase());
        }

        if (ctx.failure().getMessage() != null) {
            error.put("message", ctx.failure().getMessage());
        }

        ctx.response().setStatusCode(error.getInteger("status"));
        ctx.response().end(error.encode());
    }

    private JsonObject defaultError(String path) {
        return new JsonObject()
                .put("timestamp", System.currentTimeMillis())
                .put("status", HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .put("error", HttpResponseStatus.valueOf(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).reasonPhrase())
                .put("path", path);
    }
}
