# scenario-101-getting-started

Based on `mvn io.quarkus:quarkus-maven-plugin:999-SNAPSHOT:create     -DprojectGroupId=io.quarkus.qe     -DprojectArtifactId=scenario-101-getting-started     -DprojectVersion=1.0.0-SNAPSHOT     -DclassName="io.quarkus.qe.hello.GreetingResource"     -Dextensions=io.quarkus:quarkus-hibernate-orm,io.quarkus:quarkus-jsonb,io.quarkus:quarkus-jsonp,io.quarkus:quarkus-resteasy-jsonb,io.quarkus:quarkus-narayana-jta,io.quarkus:quarkus-elytron-security,io.quarkus:quarkus-scheduler,io.quarkus:quarkus-swagger-ui,io.quarkus:quarkus-hibernate-validator,io.quarkus:quarkus-undertow-websockets,io.quarkus:quarkus-smallrye-fault-tolerance,io.quarkus:quarkus-smallrye-metrics,io.quarkus:quarkus-smallrye-openapi,io.quarkus:quarkus-smallrye-jwt,io.quarkus:quarkus-smallrye-health` generated project

Uses MP health (https://quarkus.io/guides/microprofile-health) and MP metrics (https://quarkus.io/guides/microprofile-metrics).

Application:
- Define greeting resource with metrics.
- Define health checks.
- Define a bean to inject scoped HTTP beans.
- Define Fallback resource.

Tests:
- Test the health endpoints responses.
- Test greeting resource endpoint response.
- Reproducer for [QUARKUS-662](https://issues.redhat.com/browse/QUARKUS-662): "Injection of HttpSession throws UnsatisfiedResolutionException during the build phase" is covered by the test `InjectingScopedBeansResourceTest` and `NativeInjectingScopedBeansResourceIT`.
- Test to cover the functionality of the Fallback feature and ensure the associated metrics are properly updated. 
