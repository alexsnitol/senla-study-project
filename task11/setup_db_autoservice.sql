-- Create tables

CREATE TYPE status AS ENUM('COMPLETED',
                           'IN_PROCESS',
                           'POSTPONED',
                           'CANCELLED',
                           'PAUSED',
                           'DELETED');

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



-- Insert data

INSERT INTO garages(size)
VALUES (10),
       (5),
       (2)
;

INSERT INTO masters(last_name, first_name, patronymic)
VALUES ('Ivanov',       'Ivan',     'Ivanovich'),
       ('Naberezhneva', 'Mila',     'Evgenevna'),
       ('Merzlyakova',  'Maryana',  'Egorovna'),
       ('Baev',         'Serafim',  'Pavlovich'),
       ('YAgunov',      'Feliks',   'Aleksandrovich'),
       ('Lobachev',     'Semen',    'Evgenievich'),
       ('YUshakova',    'Marianna', 'Klimentevna'),
       ('Voloshin',     'Trofim',   'Fedorovich'),
       ('Hitrovo',      'Tamara',   'Kirillovna'),
       ('Ejler',        'Viktoriya','Konstantinovna')
;

INSERT INTO orders(time_of_created, time_of_begin, time_of_completion, status, price)
VALUES ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 'COMPLETED', 500),
       ('2022-05-01 18:29:06', '2022-06-02 18:29:06', '2022-06-03 18:29:06', 'IN_PROCESS', 400),
       ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 'COMPLETED', 600),
       ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 'IN_PROCESS', 800),
       ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 'CANCELLED', 900)
;

INSERT INTO order_garage(orders_id, garages_id, place)
VALUES (1, 1, 0),
       (2, 1, 1),
       (3, 1, 2),
       (4, 1, 3),
       (5, 2, 0)
;

INSERT INTO order_master(orders_id, masters_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (3, 5),
       (4, 6),
       (5, 7)
;