CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE ExecutionEntity
(
  id            BIGINT PRIMARY KEY,
  seconds       BIGINT NOT NULL,
  owner         VARCHAR(100) NOT NULL
);