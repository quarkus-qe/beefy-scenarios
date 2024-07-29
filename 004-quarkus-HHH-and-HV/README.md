# Quarkus - Hibernate and Hibernate Validator (lazy fetch strategy)
This scenario uses in-memory Java database H2.  
Test class annotated with `@WithTestResource(H2DatabaseTestResource.class)` starts an in-memory H2 database

Module that covers integration with some Hibernate features like:
- Reproducer for [14201](https://github.com/quarkusio/quarkus/issues/14201) and [14881](https://github.com/quarkusio/quarkus/issues/14881): possible data loss bug in hibernate. This is covered under the Java package `io.quarkus.qe.hibernate.items`.
- Reproducer for [QUARKUS-661](https://issues.redhat.com/browse/QUARKUS-661): @TransactionScoped Context does not call @Predestroy on TransactionScoped Beans. This is covered under the Java package `io.quarkus.qe.hibernate.transaction`.