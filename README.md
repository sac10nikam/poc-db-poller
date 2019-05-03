# PoC Database Poller

This is a small PoC project for building a database poller which locks the records and ignores locked records.
By using this construction it becomes possible to run multiple database pollers, without duplicates. Key to this
that the database must support it. The following databases at least support it:

- MS SQL Server
- Oracle DB
- Postgres

## Spring Data JPA

This project is build using Spring Boot with Spring Data JPA and Hibernate providing the persistance layer. Hibernate
provides support for this construction (even through Spring Data JPA), providing that:

- There is a transaction
    - Using the annotation `@Transaction`
- A pessimistic write lock is set
    - Using the annotation `@Lock(LockModeType.PESSIMISTIC_WRITE)`
- The property `javax.persistence.lock.timeout` is set to -2
    - Using the annotation `@QueryHints({@QueryHint(name="javax.persistence.lock.timeout", value="-2")})`

See the class [PollerRepository](src/main/java/ninckblokje/poc/db/poller/repository/PollerRepository.java) for an example.
 
## JUnit tests

The behaviour is tested in the class [PocDbPollerApplicationTests](src/test/java/ninckblokje/poc/db/poller/PocDbPollerApplicationTests.java).
By default Maven and the test will fail. Provide one of the following profiles to test it with the corresponding database:

- postgres
- sqlserver

For a failure, use the profile:

- mariadb

## Documentation

- https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/chapters/locking/Locking.html 