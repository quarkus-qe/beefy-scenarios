# Quarkus - many extensions in one app

## Import many extensions
Project skeleton generated via following command:

```
mvn io.quarkus:quarkus-maven-plugin:999-SNAPSHOT:create \
    -DprojectGroupId=io.quarkus.qe \
    -DprojectArtifactId=003-quarkus-many-extensions \
    -DprojectVersion=1.0.0-SNAPSHOT \
    -DplatformArtifactId=quarkus-bom \
    -DclassName="org.my.group.MyResource" \
    -Dextensions="agroal,artemis-jms,config-yaml,core,elytron-security,gizmo,hibernate-orm,hibernate-orm-panache,hibernate-validator,jackson,jaxb,jdbc-mariadb,jdbc-mssql,jdbc-mysql,jdbc-postgresql,jsonb,jsonp,kafka-client,keycloak-authorization,kubernetes,narayana-jta,oidc,quartz,reactive-pg-client,rest-client,resteasy,resteasy-jackson,resteasy-jsonb,resteasy-jaxb,scheduler,smallrye-context-propagation,smallrye-fault-tolerance,smallrye-health,smallrye-jwt,smallrye-metrics,smallrye-openapi,smallrye-reactive-messaging,smallrye-reactive-messaging-amqp,smallrye-reactive-messaging-kafka,smallrye-reactive-streams-operators,spring-data-jpa,spring-di,spring-web,spring-security,undertow,undertow-websockets,vertx"
```
