# Jbang - Run java as scripts anywhere!. 

The aim of this module, is provide several scenarios in order to test Quarkus/Jbang integration. 

## Prerequisites
 - JDK 11
 - Docker
 - Jbang (Optional)

## Scenarios

### `stockCryptoCurrency`
Simple Rest API in order to retrieve all available cryptocurrencies, and their current values on BTC, USD, and EUR. 

## How to run locally

Once you have [installed Jbang](https://www.jbang.dev/download) on your laptop/desktop, go to `src/main/java/io/quarkus/qe/jbang`
and "fire" jBang!: `stockCryptoCurrency.java` 

## Testing approach

JBang doesn't need dependency management, but we are using maven to integrate this module with the other scenarios of this project, but also to run our tests. 
So, our jBang scripts are going to be deployed on a jBang docker container, who will launch those scripts without maven/gradle and then,
at this point run our junit tests from our local environment. 
