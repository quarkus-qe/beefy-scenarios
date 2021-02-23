# Quarkus - Spring Data REST

## Scope of the testing
Used addition features and functionality:
- Hibernate Validator
- One-to-Many and Many-to-One relationship

Used quarkus-spring-data-rest repositories:
- CrudRepository
- PagingAndSortingRepository

Test for the CrudRepository:  
- Verify all the CRUD methods are available for end user

Test for the PagingAndSortingRepository:  
- Redefine default path via *path* attribute
- Define collection's root name via *collectionResourceRel* attribute
- Export only certain CRUD methods and restrict the use of others for the end-user
- Verify pagination and sorting are working correct

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM

In addition, you will need either a PostgreSQL database, or Docker to run one. Database for tests handled automatically using Testcontainers and enriched via `import.sql` file.

To run PostgreSQL database in Docker run:  
> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:11.5

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

