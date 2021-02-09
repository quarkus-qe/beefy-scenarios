package io.quarkus.qe.jbang;//usr/bin/env jbang "$0" "$@" ; exit $?

//JAVAC_OPTIONS -parameters

//DEPS io.quarkus:quarkus-bom:1.11.0.Final@pom
//DEPS io.quarkus:quarkus-vertx-web
//DEPS io.smallrye.reactive:smallrye-mutiny-vertx-web-client
//DEPS io.quarkus:quarkus-resteasy-mutiny
//DEPS io.quarkus:quarkus-smallrye-openapi
//DEPS io.quarkus:quarkus-swagger-ui

//Q:CONFIG quarkus.swagger-ui.always-include=true
//Q:CONFIG quarkus.http.root-path=/api
//Q:CONFIG quarkus.http.port=9090

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.configuration.ProfileManager;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import io.quarkus.runtime.Quarkus;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

@OpenAPIDefinition(
        info = @Info(
                title = "Crypto currency API",
                version = "1.0.0",
                contact = @Contact(name = "Crypto currency API Support", email = "techsupport@example.com"),
                license = @License(name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
@Tag(name = "stock", description = "crypto currency stock values")
@RouteBase(path = "/stock", produces = "application/json")
@ApplicationScoped
public class stockCryptoCurrency {
    public static final Logger LOG = Logger.getLogger(stockCryptoCurrency.class);
    public static final String CRYPTO_PATH_ENV = "CRYPTO_PATH";
    private static final String TEMPLATE = "Hello, %s!";

    @Inject
    private Vertx vertx;

    // TODO: looks that doesn't pick up properties that are not quarkus properties.
    static String cryptoPath = "https://min-api.cryptocompare.com";
    static final int TIMEOUT = 30;
    static final int RETRIES_AMOUNT = 3;

    private WebClient client;

    public static void main(String... args) {
        Quarkus.run(args);
    }

    @PostConstruct
    void initialize() {
        LOG.info("The application is starting with profile " + ProfileManager.getActiveProfile());
        this.client = WebClient.create(vertx);
        ObjectMapper mapper = DatabindCodec.mapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //TODO: this will not be required if we can use custom properties
        Optional.ofNullable(System.getenv(CRYPTO_PATH_ENV)).ifPresent(value -> cryptoPath = value);
    }

    ////////////////////////////////////////////////
    // Endpoints
    ///////////////////////////////////////////////

    @Route(methods = HttpMethod.GET, path = "/currency/names")
    Uni<CryptoCurrencyIndex> currencyNames() {
        return new CryptoCurrencyIndex().retrieveCryptoCurrenciesNames();
    }

//    @Route(methods = HttpMethod.GET, path = "/currency/values")
//    Uni<List<CryptoCurrencyValues>> currencyValues() {
//        return new CryptoCurrencyIndex().retrieveCryptoCurrenciesNames()
//                .onItem()
//                .transformToUni(currencies -> new CryptoCurrencyValues().getCurrencyValues(currencies.index));
//    }

    @Route(methods = HttpMethod.GET, path = "/currency/:currency/value")
    Uni<CryptoCurrencyValues> currencyValue(@Param("currency") String currency) {
        return new CryptoCurrencyValues().getCurrencyValue(currency);
    }

    @Route(methods = HttpMethod.GET, path = "/hello/:name")
    void helloWorld(RoutingContext ctx, @Param("name") String name) {
        ctx.response().end(Json.encode(new Hello(String.format(TEMPLATE, name))));
    }

    ////////////////////////////////////////////////
    // Domain
    ///////////////////////////////////////////////

    class Hello {
        public String name;
        public Hello(String name) {
            this.name = name;
        }
    }

    enum Currency{EUR, USD, BTC}

    class CryptoCurrencyIndex {
        public Set<String> index;
        public CryptoCurrencyIndex() {}
        private CryptoCurrencyIndex(Set<String> index) {
            this.index = index;
        }
        public Uni<CryptoCurrencyIndex> retrieveCryptoCurrenciesNames() {
            return client.getAbs(cryptoPath + "/data/all/coinlist")
                    //.putHeader("Accept", "application/json")
                    .send()
                    .map(resp -> new CryptoCurrencyIndex(resp.bodyAsJsonObject().getJsonObject("Data").fieldNames()))
                    .ifNoItem().after(Duration.ofSeconds(TIMEOUT)).fail()
                    .onFailure().retry().atMost(RETRIES_AMOUNT);
        }
    }

    class CryptoCurrencyValues {
        public String name;
        public double bitcoin;
        public double uniteStateDollar;
        public double euro;

        public CryptoCurrencyValues() {
        }

        private CryptoCurrencyValues(String currencyName, double btc, double usd, double eur) {
            this.name = currencyName;
            this.bitcoin = btc;
            this.uniteStateDollar = usd;
            this.euro = eur;
        }

        private CryptoCurrencyValues from(String name, JsonObject json) {
            Double btcValue = json.getJsonObject(name).getDouble(Currency.BTC.name());
            Double usdValue = json.getJsonObject(name).getDouble(Currency.USD.name());
            Double eurValue = json.getJsonObject(name).getDouble(Currency.EUR.name());

            return new CryptoCurrencyValues(name, btcValue, usdValue, eurValue);
        }

        public Uni<CryptoCurrencyValues> getCurrencyValue(String currency) {
            String currencies = Arrays.stream(Currency.values()).map(m -> m.name()).collect(Collectors.joining(","));
            return client.getAbs(cryptoPath + "/data/pricemulti?fsyms=" + currency + "&tsyms=" + currencies)
                    // .putHeader("Accept", "application/json")
                    .send()
                    .map(resp -> from(currency, resp.bodyAsJsonObject()))
                    .ifNoItem().after(Duration.ofSeconds(TIMEOUT)).fail()
                    .onFailure().retry().atMost(RETRIES_AMOUNT);
        }

        // TODO: Doesn't support .collect() - compile time issue
//        public Uni<List<CryptoCurrencyValues>> getCurrencyValues(Set<String> currencies) {
//            List<CryptoCurrencyValues> result = new ArrayList<>();
//            return Multi.createFrom().iterable(currencies).onItem()
//                    .transformToUniAndMerge(this::getCurrencyValue)
//                    .collect()
//                    .in(ArrayList::new, List::add);
//
//        }
    }
}