--Otherwise I get Caused by: org.hibernate.tool.schema.spi.SchemaManagementException: Schema-validation: sequence [service_SEQ] defined inconsistent increment-size; found [1] but expecting [50]
CREATE SEQUENCE application_SEQ START 1 increment by 50;
CREATE SEQUENCE service_SEQ START 1 increment by 50;

CREATE TABLE application
(
  id            BIGINT PRIMARY KEY,
  name          VARCHAR(100) NOT NULL UNIQUE
);