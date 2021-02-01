
CREATE TABLE airports (
  id            SERIAL PRIMARY KEY,
  iata_code     VARCHAR(100) NOT NULL UNIQUE,
  city          VARCHAR(100) NOT NULL
);

CREATE TABLE airlines (
  id            SERIAL PRIMARY KEY,
  iata_code     VARCHAR(100) NOT NULL UNIQUE,
  name          VARCHAR(100) NOT NULL,
  infant_price  FLOAT(3)
);

CREATE TABLE flights (
  id            SERIAL PRIMARY KEY,
  origin        VARCHAR(100) NOT NULL,
  destination   VARCHAR(100) NOT NULL,
  flight_code       VARCHAR(100) NOT NULL,
  base_price    INTEGER NOT NULL
);

CREATE TABLE pricingRules (
  id                     SERIAL PRIMARY KEY,
  days_to_departure        INTEGER NOT NULL,
  until                  INTEGER NOT NULL,
  percentage             INTEGER NOT NULL
);

CREATE TABLE address (
  id                     SERIAL PRIMARY KEY,
  street                 VARCHAR NOT NULL,
  block_number            VARCHAR NOT NULL,
  zip_code                VARCHAR NOT NULL,
  city                   VARCHAR NOT NULL,
  country                VARCHAR NOT NULL,
  created_at             BIGINT NOT NULL,
  updated_at             BIGINT
);

CREATE TABLE passenger (
  id              SERIAL PRIMARY KEY,
  nif             VARCHAR NOT NULL,
  name            VARCHAR NOT NULL,
  last_name       VARCHAR NOT NULL,
  contact_number  VARCHAR NOT NULL,
  created_at      BIGINT NOT NULL,
  updated_at      BIGINT,
  address_id      SERIAL,
  CONSTRAINT fk_address FOREIGN KEY(address_id) REFERENCES address(id) ON DELETE SET NULL
);

CREATE TABLE basket (
  id              SERIAL PRIMARY KEY,
  flight          VARCHAR NOT NULL,
  price           NUMERIC NOT NULL,
  created_at      BIGINT NOT NULL,
  updated_at      BIGINT,
  passenger_id    SERIAL,
  CONSTRAINT fk_passenger FOREIGN KEY(passenger_id) REFERENCES passenger(id) ON DELETE SET NULL
);

