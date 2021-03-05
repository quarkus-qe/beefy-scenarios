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
