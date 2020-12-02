CREATE TABLE version
(
  id          VARCHAR(100) PRIMARY KEY
);

ALTER TABLE application ADD COLUMN version_id VARCHAR(100) REFERENCES version(id);