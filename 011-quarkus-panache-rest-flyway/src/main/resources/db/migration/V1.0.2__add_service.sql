CREATE TABLE service
(
  id              BIGINT PRIMARY KEY,
  name            VARCHAR(100) NOT NULL,
  application_id  BIGINT NOT NULL REFERENCES application(id)
);