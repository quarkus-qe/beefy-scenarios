# Quarkus - Cover application properties setup
This module tests different ways of configuring properties for Quarkus application

1. Configuring custom user properties through nested static class (done for Antagonist)  
1.1 Configuration properties located in the *application.properties* file

2. Configuring custom user properties through set of interfaces (done for Protagonist)  
2.1 Configuration properties located in the *application.yaml* file (this file takes precedence over the
*application.properties* file)

3. Configuring custom config source provider  
3.1 File org.eclipse.microprofile.config.spi.ConfigSourceProvider must be created under **META-INF/services/**
    and contains fully qualified names of implementations  
3.2 Source provider in this example has ordinal(priority) value of 999, which means it should take precedence over the
value located in .env file.

4. Configuring custom converter  
4.1 File org.eclipse.microprofile.config.spi.Converter must be created under **META-INF/services/**
and contains fully qualified names of implementations  
4.2 To create the custom converter POJO class must be created if you're not overriding any of the
supported classes (e.x. String, Boolean, Double, etc.)

5. Configuring using injection of Bulk Of Properties into an Scoped Bean that was introduced in MicroProfile Config 2.0

6. Injecting a `ConfigValue` instance that was introduced in MicroProfile Config 2.0

7. Injecting properties using the `@ConfigMapping` annotation. More info in: https://smallrye.io/docs/smallrye-config/mapping/mapping.html

8. Injecting a map using the `@ConfigProperty` annotation. More info in: https://github.com/quarkusio/quarkus/issues/17269

___
#### Wiki:
**Protagonist**: the leading character in the story, whose purpose is to move story to its final  
**Antagonist**: the adversary of the leading character (or protagonist) in the story, whose purpose is to
interfere with the protagonist