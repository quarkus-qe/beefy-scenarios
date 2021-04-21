package io.quarkus.qe.vertx.sql.handlers;

import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolationException;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.db2client.DB2Exception;
import io.vertx.mysqlclient.MySQLException;
import io.vertx.pgclient.PgException;

@ApplicationScoped
public class FailureHandler {

    private static final int BAD_REQUEST_CODE = HttpResponseStatus.BAD_REQUEST.code();

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    void databaseConstraintFailure(PgException e, HttpServerResponse response) {
        response.setStatusCode(BAD_REQUEST_CODE).end(Json.encode(new JsonObject().put("msg", e.getMessage())));
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    void databaseMysqlConstraintFailure(MySQLException e, HttpServerResponse response) {
        response.setStatusCode(BAD_REQUEST_CODE).end(Json.encode(new JsonObject().put("msg", e.getMessage())));
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    void databaseDb2ConstraintFailure(DB2Exception e, HttpServerResponse response) {
        response.setStatusCode(BAD_REQUEST_CODE).end(Json.encode(new JsonObject().put("msg", e.getMessage())));
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    public void exceptions(ConstraintViolationException e, HttpServerResponse res) {
        res.setStatusCode(BAD_REQUEST_CODE).end(handler ->
                e.getConstraintViolations().stream()
                        .map(err -> String.format("%s: %s", err.getPropertyPath().toString(), err.getMessage()))
                        .collect(Collectors.joining("\n"))
        );
    }

}
