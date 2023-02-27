CREATE SEQUENCE ExecutionEntity_SEQ START with 1 increment by 1;

CREATE TABLE ExecutionEntity
(
  id            BIGINT PRIMARY KEY,
  seconds       BIGINT NOT NULL,
  owner         VARCHAR(100) NOT NULL
);