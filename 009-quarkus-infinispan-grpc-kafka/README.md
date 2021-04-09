# Table of Contents
1. [Quarkus infinispan grpc kafka](#Quarkus-infinispan-grpc-kafka)
2. [Quarkus SSL/TLS Infinispan scenario ](#Quarkus-SSL/TLS-Infinispan-scenario )
3. [Quarkus Grateful Shutdown for Kafka connectors](#Quarkus-Grateful-Shutdown-for-Kafka-connectors)

## Quarkus SSL/TLS infinispan grpc kafka
Module that test whether gRPC, Infinispan and Kafka extensions work together:
- for gRPC: there is a simple greetings endpoint. This example will use a `helloworld.proto` file to generate the required sources. 
- for Infinispan: to check whether the cache persistence is working fine
- for Kafka: to verify the messages are working in a chain workflow.

## Quarkus SSL/TLS Infinispan scenario 

##hotrod-client.properties
Test that hotrod-client.properties file is picked up at the build time (should be placed under src/main/resources/META-INF).    
Infinispan Server mimicked by Testcontainers.

##Infinispan server SSL/TLS
Test SSL/TLS secure connection between Infinispan client and Infinispan server.  
Client and server are using the same `server.jks` file for authentication (client truststore / server keystore)  
Information about Infinispan server configuration can be found at [this Github page](https://github.com/infinispan/infinispan-images)

## Quarkus Grateful Shutdown for Kafka connectors
This scenario covers the fix for [QUARKUS-858](https://issues.redhat.com/browse/QUARKUS-858): Avoid message loss during the graceful shutdown (SIGTERM) of the Kafka connector.
The test will confirm that no messages are lost when the `grateful-shutdown` is enabled. In the other hand, when this property is disabled, messages might be lost.