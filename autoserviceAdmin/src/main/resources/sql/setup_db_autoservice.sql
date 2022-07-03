-- Create tables

CREATE SEQUENCE orders_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE masters_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE garages_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE order_master_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE order_garage_id_seq START WITH 100 INCREMENT BY 1;


CREATE TABLE orders (
  id                 INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('orders_id_seq'),
  time_of_created    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  time_of_begin      TIMESTAMP, 
  time_of_completion TIMESTAMP,
  price              FLOAT4 DEFAULT 0,
  status             VARCHAR(10) DEFAULT 'IN_PROCESS'
  );

CREATE TABLE masters (
  id         INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('masters_id_seq'),
  last_name  VARCHAR(255),
  first_name VARCHAR(255),
  patronymic VARCHAR(255)
  );

CREATE TABLE garages (
  id     INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('garages_id_seq'),
  "size" INT4 DEFAULT 0 NOT NULL
  );

CREATE TABLE order_master (
  id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('order_master_id_seq'),
  order_id  INT8 NOT NULL,
  master_id INT8 NOT NULL,

  FOREIGN KEY (master_id) REFERENCES masters (id),
  FOREIGN KEY (order_id) REFERENCES orders (id)
  );

CREATE TABLE order_garage (
  id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('order_garage_id_seq'),
  order_id  INT8 NOT NULL,
  garage_id INT8 NOT NULL,
  place     INT4 NOT NULL,

  FOREIGN KEY (order_id) REFERENCES orders (id),
  FOREIGN KEY (garage_id) REFERENCES garages (id)
  );


ALTER SEQUENCE orders_id_seq OWNED BY orders.id;
ALTER SEQUENCE masters_id_seq OWNED BY masters.id;
ALTER SEQUENCE garages_id_seq OWNED BY garages.id;
ALTER SEQUENCE order_master_id_seq OWNED BY order_master.id;
ALTER SEQUENCE order_garage_id_seq OWNED BY order_garage.id;


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
       ('Yagunov',      'Feliks',   'Aleksandrovich'),
       ('Lobachev',     'Semen',    'Evgenievich'),
       ('Yushakova',    'Marianna', 'Klimentevna'),
       ('Voloshin',     'Trofim',   'Fedorovich'),
       ('Hitrovo',      'Tamara',   'Kirillovna'),
       ('Ejler',        'Viktoriya','Konstantinovna')
;

INSERT INTO orders(time_of_created, time_of_begin, time_of_completion, price, status)
VALUES ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 500, 'COMPLETED'),
       ('2022-05-01 18:29:06', '2022-06-02 18:29:06', '2022-06-03 18:29:06', 400, 'IN_PROCESS'),
       ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 600, 'COMPLETED'),
       ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 800, 'IN_PROCESS'),
       ('2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 900, 'CANCELLED')
;

INSERT INTO order_garage(order_id, garage_id, place)
VALUES (100, 100, 0),
       (101, 100, 1),
       (102, 100, 2),
       (103, 100, 3),
       (104, 101, 0)
;

INSERT INTO order_master(order_id, master_id)
VALUES (100, 100),
       (100, 101),
       (100, 102),
       (101, 103),
       (102, 104),
       (103, 105),
       (104, 106)
;