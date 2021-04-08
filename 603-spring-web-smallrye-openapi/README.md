# Table of contents
1. [Quarkus Spring Web + Quarkus SmallRye OpenAPI](#quarkus-spring-web--quarkus-smallrye-openapi)
1. [Quarkus Spring Web - Spring Boot Bootstrap application](#quarkus-spring-web---spring-boot-bootstrap-application)

# Quarkus Spring Web + Quarkus SmallRye OpenAPI

## Issues references
[SmallRye OpenAPI generates incorrect content type when using Spring Web annotations](https://issues.redhat.com/browse/QUARKUS-849)

## Scope of the testing
Verify correct content types in OpenAPI endpoint output (`/q/openapi`).
- Define application endpoints using Spring Web annotations.
    - Use all available REST annotations: `@RequestMapping`, `@DeleteMapping`, `@GetMapping`, `@PatchMapping`, `@PostMapping`, `@PutMapping`.
    - Define content type using attributes of these annotations. Where possible, define request and response content type.
    - Combine content type definition on class and method level.
- Generate OpenAPI schema using `quarkus-smallrye-openapi`.
- Access the OpenAPI schema by invoking `/q/openapi` endpoint.
- Verify content types in the schema. The types should correspond to the definitions in Spring annotations.

## Requirements
- JDK 1.8+
- GraalVM

# Quarkus Spring Web - Spring Boot Bootstrap application

## Issues references
[Document: Using the Quarkus Extension for Spring Web API](https://issues.redhat.com/browse/QUARKUS-185)

[Spring Web: @ExceptionHandler response does not override @ResponseStatus](https://github.com/quarkusio/quarkus/issues/16321)

## Scope of the testing
Test CRUD resource `BookController`:
- Verify response status.
- Verify modified data in write operations.
- Verify custom error handler.

Test home page HTML response and value injection in `SimpleController`.

## Requirements
- JDK 1.8+
- GraalVM
