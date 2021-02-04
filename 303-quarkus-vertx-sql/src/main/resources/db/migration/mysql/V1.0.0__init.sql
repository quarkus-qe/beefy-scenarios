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
  id                     INT PRIMARY KEY AUTO_INCREMENT,
  street                 VARCHAR(300) NOT NULL,
  block_number           VARCHAR(20) NOT NULL,
  zip_code               VARCHAR(20) NOT NULL,
  city                   VARCHAR(150) NOT NULL,
  country                VARCHAR(200) NOT NULL,
  created_at             BIGINT NOT NULL,
  updated_at             BIGINT
);

CREATE TABLE passenger (
  id              INT PRIMARY KEY AUTO_INCREMENT,
  nif             VARCHAR(10) NOT NULL,
  name            VARCHAR(25) NOT NULL,
  last_name       VARCHAR(55) NOT NULL,
  contact_number  VARCHAR(20) NOT NULL,
  created_at      BIGINT NOT NULL,
  updated_at      BIGINT,
  address_id      INT,
  FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE SET NULL
);

CREATE TABLE basket (
  id              INT PRIMARY KEY AUTO_INCREMENT,
  flight          VARCHAR(10) NOT NULL,
  price           NUMERIC NOT NULL,
  created_at      BIGINT NOT NULL,
  updated_at      BIGINT,
  passenger_id    INT,
  FOREIGN KEY (passenger_id) REFERENCES passenger(id) ON DELETE SET NULL
);

