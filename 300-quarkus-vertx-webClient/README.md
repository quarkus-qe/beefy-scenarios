# Quarkus Vertx Webclient Ext

## Recommended Readings

[Vert.x Web Clients](https://quarkus.io/guides/vertx#using-vert-x-clients)

## Requirements

To compile and run this demo you will need:

- JDK 11+

## Scope of the testing

Vert.x Mutiny webClient exploratory test. 
* Vert.x WebClient
* Quarkus Resteasy Mutiny / Jsonb
* Quarkus configuration converters
* Exception mapper
* Automating testing

**Brief description:** This application is a third party proxy. Basically deliver Chuck Norris quotes through three endpoints:
* `/chuck` request a random quote and parse the response with a Vert.x Mutiny `mapTo` method.
* `/chuck/bodyCodec` does exactly the same as `/chuck` but the response is parsed by a JSON body codec. The result must be the same as `/chuck` 
* `/chuck/combine` request two random quotes at the same time, and combine both quotes in a single result.  

## Live coding with Quarkus

> mvn quarkus:dev

Available endpoints:
> curl -v http://localhost:8080/chuck
>
> curl -v http://localhost:8080/chuck/bodyCodec
>
> curl -v http://localhost:8080/chuck/combine
>
### Run automated tests
 > mvn test