# Table of Contents
1. [Quarkus Vertx Kafka Ext](#quarkus-vertx-kafka-ext)

## Quarkus Vertx Kafka Ext

### Recommended Readings

[Quarkus Apache Kafka Reactive Messaging](https://quarkus.io/guides/kafka)

[smallrye-reactive-messaging Kafka](https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/2/kafka/kafka.html)

[AVRO serializer/deserializers](https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/2.2/kafka/kafka.html#kafka-avro-configuration)

[Test profiles introduction](https://quarkus.io/blog/quarkus-test-profiles/)

[Test Profiles: writing your profile](https://quarkus.io/guides/getting-started-testing#writing-a-profile)

### Requirements

To compile and run this demo you will need:

- JDK 11+
- Docker / docker-compose

### Scope of the testing

Vert.x Kafka exploratory test. 
* Kafka producer
* Kafka consumer
* AVRO serializers/deserializers
* Application property profiles
* Deploy Verticles
* Automating testing
* Test profiles (Strimzi/Confluent)

**Brief description:** A [verticle][1] deployer launch a [periodic verticle][2] that is going to be producing random Stock-market values and push it to a Kafka Topic (`stock-price`). 
A reactive kafka consumer (`quarkus-smallrye-reactive-messaging-kafka`) will be subscribed to this topic and will change the status of the incoming events from `pending` to `completed`, and moving these records to `end-stock-price` topic. 
Note that there is another verticle `KStockPriceConsumer` launched by our verticle deployer, that does nothing. The reason is because `quarkus-smallrye-reactive-messaging-kafka` is already using vertx kafka client, 
so we don't really need a new verticle to consume Kafka in a reactive Non-blocking way. However, this verticle can be an example about how to deploy an abstract verticle from a verticle deployer.   
 
 [1]: https://vertx.io/docs/vertx-core/java/#_verticles
 [2]: https://vertx.io/docs/vertx-core/java/#_executing_periodic_and_delayed_actions
 
### Live coding with Quarkus

#### Strimzi:

> docker-compose -f docker-compose-strimzi.yaml up
>
> mvn quarkus:dev

#### Confluent:
> docker-compose -f docker-compose-confluent.yaml up
>
> mvn -Dquarkus.profile=confluent quarkus:dev

#### Run automated tests
> mvn test

### Troubleshooting

#### When I swap from Strimzi/confluent sometimes Kafka broker doesn't weak up (docker-compose).
Is something that We should investigate, must be for some service name collisions. It's the middle time you could clean your docker containers by running the following commands:
> docker stop $(docker ps -a -q)
>
> docker rm $(docker ps -a -q)

#### [Only Fedora] When I launch a docker compose, kafka broker can't reach Zookeper node rather a internal network issue. 
`error: kafka.zookeeper.ZooKeeperClientTimeoutException: Timed out waiting for connection while in state: CONNECTING

Looks that the default backend for firewalld was changed from iptables to nftables, since Fedora32. A quick patch it's a rollback to iptables as firewalld backend.
> sudo sed -i 's/FirewallBackend=nftables/FirewallBackend=iptables/g' /etc/firewalld/firewalld.conf
>
> sudo systemctl restart firewalld docker
