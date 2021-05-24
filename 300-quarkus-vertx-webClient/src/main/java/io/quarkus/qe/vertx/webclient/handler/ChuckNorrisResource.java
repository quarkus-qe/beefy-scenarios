package io.quarkus.qe.vertx.webclient.handler;

import static io.quarkus.vertx.web.Route.HttpMethod;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.qe.vertx.webclient.config.ChuckEndpointValue;
import io.quarkus.qe.vertx.webclient.config.VertxWebClientConfig;
import io.quarkus.qe.vertx.webclient.model.Joke;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.client.predicate.ResponsePredicate;
import io.vertx.mutiny.ext.web.codec.BodyCodec;

@RouteBase(path = "/chuck")
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

    @Route(methods = HttpMethod.GET, path = "/")
    public Uni<Joke> getRandomJoke() {
        return getChuckQuoteAsJoke()
                .ifNoItem().after(Duration.ofSeconds(httpClientConf.timeout())).fail()
                .onFailure().retry().atMost(httpClientConf.retries());
    }

    @Route(methods = HttpMethod.GET, path = "/bodyCodec", produces = "application/json")
    public Uni<Joke> getRandomJokeWithBodyCodec() {
        return client.getAbs(chuckNorrisQuote.getValue())
                .as(BodyCodec.json(Joke.class))
                .putHeader("Accept", "application/json")
                .expect(ResponsePredicate.status(HttpURLConnection.HTTP_OK))
                .send()
                .map(HttpResponse::body)
                .ifNoItem().after(Duration.ofSeconds(httpClientConf.timeout())).fail()
                .onFailure().retry().atMost(httpClientConf.retries());
    }

    @Route(methods = HttpMethod.GET, path = "/combine", produces = "application/json")
    public Uni<List<Joke>> getTwoRandomJokes() {
        Uni<Joke> jokeOne = getChuckQuoteAsJoke();
        Uni<Joke> jokeTwo = getChuckQuoteAsJoke();

        return Uni.combine()
                .all()
                .unis(jokeOne, jokeTwo)
                .combinedWith((BiFunction<Joke, Joke, List<Joke>>) Arrays::asList);
    }

    @Route(methods = HttpMethod.GET, path = "/pong", produces = "application/json")
    public Uni<String> ping() {
        return Uni.createFrom().item("pong");
    }

    private Uni<Joke> getChuckQuoteAsJoke() {
        return client.getAbs(chuckNorrisQuote.getValue())
                .putHeader("Accept", "application/json")
                .expect(ResponsePredicate.status(HttpURLConnection.HTTP_OK))
                .send()
                .map(resp -> resp.bodyAsJsonObject().mapTo(Joke.class));
    }
}
