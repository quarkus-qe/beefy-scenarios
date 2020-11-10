Module that test whether gRPC, Infinispan and Kafka extensions work together:
- for gRPC: there is a simple greetings endpoint. This example will use a `helloworld.proto` file to generate the required sources. 
- for Infinispan: to check whether the cache persistence is working fine
- for Kafka: to verify the messages are working in a chain workflow.