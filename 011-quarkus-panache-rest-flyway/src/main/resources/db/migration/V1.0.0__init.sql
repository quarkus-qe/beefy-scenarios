CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE application
(
  id            BIGINT PRIMARY KEY,
  name          VARCHAR(100) NOT NULL UNIQUE
);