# Quarkus - Infinispan scenario

##hotrod-client.properties
Test that hotrod-client.properties file is picked up at the build time (should be placed under src/main/resources/META-INF).    
Infinispan Server mimicked by Testcontainers.

##Infinispan server SSL/TLS
Test SSL/TLS secure connection between Infinispan client and Infinispan server.  
Client and server are using the same `server.jks` file for authentication (client truststore / server keystore)  
Information about Infinispan server configuration can be found at [this Github page](https://github.com/infinispan/infinispan-images)