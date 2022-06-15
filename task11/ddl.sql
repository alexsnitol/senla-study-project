DROP TABLE IF EXISTS order_master;
DROP TABLE IF EXISTS order_garage;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS masters;
DROP TABLE IF EXISTS garages;

-- CREATE TYPE status AS ENUM('COMPLETED',
--                            'IN_PROCESS',
--                            'POSTPONED',
--                            'CANCELLED',
--                            'PAUSED',
--                            'DELETED');

CREATE TABLE orders (
  id                 SERIAL PRIMARY KEY NOT NULL,
  time_of_created    timestamp NOT NULL, 
  time_of_begin      timestamp, 
  time_of_completion timestamp, 
  status             status,
  price              float4
  );

CREATE TABLE masters (
  id         SERIAL PRIMARY KEY NOT NULL,
  last_name  char(255), 
  first_name char(255), 
  patronymic char(255)
  );

CREATE TABLE garages (
  id     SERIAL PRIMARY KEY NOT NULL,
  "size" int4 NOT NULL
  );

CREATE TABLE order_master (
  orders_id  int8 NOT NULL, 
  masters_id int8 NOT NULL,

  FOREIGN KEY (masters_id) REFERENCES masters (id),
  FOREIGN KEY (orders_id) REFERENCES orders (id)
  );

CREATE TABLE order_garage (
  orders_id  int8 NOT NULL, 
  garages_id int8 NOT NULL, 
  place      int4 NOT NULL,

  FOREIGN KEY (orders_id) REFERENCES orders (id),
  FOREIGN KEY (garages_id) REFERENCES garages (id)
  );