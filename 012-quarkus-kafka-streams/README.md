# Quarkus Kafka Streams

## Recommended Readings

[Quarkus Apache Kafka Streams](https://quarkus.io/guides/kafka-streams)

## Requirements

To compile and run this demo you will need:

- JDK 11+
- Docker / docker-compose

## Scope of the testing

### Kafka Streams
Kafka streams 
* Kafka producer
* Kafka consumer
* Kafka stream
* Kafka stream 'windowed'
* Automating testing (JVM/native)

**Brief description:** Login monitor alerts. This kafka stream will handle login status events, and will group these events in fixed time windows. 
So if the number of wrong access excess a threshold, then a new alert event is thrown. All aggregated events(not only unauthorized) are persisted.   

![Architecture Diagram](docs/LoginMonitor.png)


### Quarkus Grateful Shutdown for Kafka connectors

This scenario covers the fix for [QUARKUS-858](https://issues.redhat.com/browse/QUARKUS-858): Avoid message loss during the graceful shutdown (SIGTERM) of the Kafka connector.
The test will confirm that no messages are lost when the `grateful-shutdown` is enabled. In the other hand, when this property is disabled, messages might be lost.

## Live coding with Quarkus

Monitor url: `http://localhost:8080/monitor/stream`

### Strimzi:

> docker-compose -f docker-compose-strimzi.yaml up
>
> mvn quarkus:dev

### Confluent:
> docker-compose -f docker-compose-confluent.yaml up
>
> mvn quarkus:dev

### Run automated tests
> mvn test
