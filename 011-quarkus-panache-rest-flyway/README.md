# REST API with Panache and Flyway

Module that test whether we can setup a REST API using ORM Panache with Flyway and a PostgreSQL database.

Topics covered here:
- https://quarkus.io/guides/hibernate-orm-panache
- https://quarkus.io/guides/rest-data-panache
- https://quarkus.io/guides/flyway

In the future, I would like to cover different database at the same time.

## Disabled tests

`PostgreSqlApplicationResourceTest#shouldReturnBadRequestIfApplicationNameIsNull` - see https://github.com/quarkusio/quarkus/issues/13307
