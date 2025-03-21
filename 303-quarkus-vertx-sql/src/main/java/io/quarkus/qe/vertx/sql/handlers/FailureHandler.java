package io.quarkus.qe.vertx.sql.handlers;

import java.util.stream.Collectors;

import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLException;
import io.vertx.pgclient.PgException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;

@ApplicationScoped
public class FailureHandler {

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    void databaseConstraintFailure(PgException e, HttpServerResponse response) {
        response.setStatusCode(400).end(Json.encode(new JsonObject().put("msg", e.getMessage())));
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    void databaseMysqlConstraintFailure(MySQLException e, HttpServerResponse response) {
        response.setStatusCode(400).end(Json.encode(new JsonObject().put("msg", e.getMessage())));
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, produces = "application/json")
    public void exceptions(ConstraintViolationException e, HttpServerResponse res) {
        res.setStatusCode(400).end(handler -> e.getConstraintViolations().stream()
                .map(err -> String.format("%s: %s", err.getPropertyPath().toString(), err.getMessage()))
                .collect(Collectors.joining("\n")));
    }

}
