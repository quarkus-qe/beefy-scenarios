# Quarkus Kafka Streams (moved to [301-quarkus-vertx-kafka](../301-quarkus-vertx-kafka))

## Recommended Readings

[Quarkus Apache Kafka Streams](https://quarkus.io/guides/kafka-streams)

## Requirements

To compile and run this demo you will need:

- JDK 11+
- Docker / docker-compose

## Scope of the testing

Kafka streams 
* Kafka producer
* Kafka consumer
* Kafka stream
* Kafka stream 'windowed'
* Automating testing (JVM/native)

**Brief description:** Login monitor alerts. This kafka stream will handle login status events, and will group these events in fixed time windows. 
So if the number of wrong access excess a threshold, then a new alert event is thrown. All aggregated events(not only unauthorized) are persisted.   

![Architecture Diagram](docs/LoginMonitor.png)

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
