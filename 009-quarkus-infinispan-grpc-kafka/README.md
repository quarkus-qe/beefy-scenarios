# Table of Contents
1. [Quarkus infinispan grpc kafka](#Quarkus-infinispan-grpc-kafka)
2. [Quarkus SSL/TLS Infinispan scenario ](#Quarkus-SSL/TLS-Infinispan-scenario )
3. [Quarkus Grateful Shutdown for Kafka connectors](#Quarkus-Grateful-Shutdown-for-Kafka-connectors)

## Quarkus SSL/TLS infinispan grpc kafka
Module that test whether gRPC, Infinispan and Kafka extensions work together:
- for gRPC: there is a simple greetings endpoint. This example will use a `helloworld.proto` file to generate the required sources. 
- for Infinispan: to check whether the cache persistence is working fine
- for Kafka: to verify the messages are working in a chain workflow.

## Kafka-client SSL / SASL
Test SSL and SASL authentication through Quarkus Kafka client extension. 
We have two endpoints `SaslKafkaEndpoint` and `SslKafkaEndpoint` that are able to produce events into kafka
and consume events and check topics through `AdminClient` and `KafkaConsumer`.

## Quarkus SSL/TLS Infinispan scenario 

##hotrod-client.properties
Test that hotrod-client.properties file is picked up at the build time (should be placed under src/main/resources/META-INF).    
Infinispan Server mimicked by Testcontainers.

##Infinispan server SSL/TLS
Test SSL/TLS secure connection between Infinispan client and Infinispan server.  
Client and server are using the same `server.jks` file for authentication (client truststore / server keystore)  
Information about Infinispan server configuration can be found at [this Github page](https://github.com/infinispan/infinispan-images)
