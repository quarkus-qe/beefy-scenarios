package io.quarkus.qe.vertx.webclient;

import io.quarkus.qe.vertx.webclient.config.ChuckEndpointValue;
import io.quarkus.qe.vertx.webclient.config.VertxWebClientConfig;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.client.predicate.ResponsePredicate;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import javax.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

@Path("/chuck")
public class ChuckNorrisResource {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "chucknorris.api.domain")
    ChuckEndpointValue chuckNorrisQuote;

    @Inject
    VertxWebClientConfig httpClientConf;

    private WebClient client;

    @PostConstruct
    void initialize() {
        this.client = WebClient.create(vertx);
    }

    @Transactional
    @Scheduled(cron = "0/1 * * * * ?")
    void increment() {
        System.out.println("hello Scheduler");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Uni<Response> getRandomJoke() {
        return getChuckQuoteAsJoke()
                .map(resp -> Response.ok(resp).build())
                .ifNoItem().after(Duration.ofSeconds(httpClientConf.timeout)).fail()
                .onFailure().retry().atMost(httpClientConf.retries);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/bodyCodec")
    public Uni<Response> getRandomJokeWithBodyCodec() {
          return client.getAbs(chuckNorrisQuote.getValue())
                .as(BodyCodec.json(Joke.class))
                .putHeader("Accept", "application/json")
                .expect(ResponsePredicate.status(Response.Status.OK.getStatusCode()))
                .send()
                .map(resp -> Response.ok(resp.body()).build())
                .ifNoItem().after(Duration.ofSeconds(httpClientConf.timeout)).fail()
                .onFailure().retry().atMost(httpClientConf.retries);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/combine")
    public Uni<Response> getTwoRandomJokes() {
        Uni<Joke> jokeOne = getChuckQuoteAsJoke();
        Uni<Joke> jokeTwo = getChuckQuoteAsJoke();

        return Uni.combine()
                .all()
                .unis(jokeOne, jokeTwo)
                .combinedWith((BiFunction<Joke, Joke, List<Joke>>) Arrays::asList)
                .map(resp -> Response.ok(Json.encode(resp)).build());
    }

    private Uni<Joke> getChuckQuoteAsJoke() {
        return client.getAbs(chuckNorrisQuote.getValue())
                .putHeader("Accept", "application/json")
                .expect(ResponsePredicate.status(Response.Status.OK.getStatusCode()))
                .send()
                .map(resp -> resp.bodyAsJsonObject().mapTo(Joke.class));
    }

}

