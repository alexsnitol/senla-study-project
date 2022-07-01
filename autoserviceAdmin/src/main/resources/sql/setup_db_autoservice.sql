-- Create tables

CREATE TYPE order_status AS ENUM(
    'COMPLETED',
    'IN_PROCESS',
    'POSTPONED',
    'CANCELLED',
    'PAUSED',
    'DELETED'
    );


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
  status             order_status DEFAULT 'IN_PROCESS'
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

INSERT INTO garages(id, size)
VALUES (101, 10),
       (102, 5),
       (103, 2)
;

INSERT INTO masters(id, last_name, first_name, patronymic)
VALUES (101, 'Ivanov',       'Ivan',     'Ivanovich'),
       (102, 'Naberezhneva', 'Mila',     'Evgenevna'),
       (103, 'Merzlyakova',  'Maryana',  'Egorovna'),
       (104, 'Baev',         'Serafim',  'Pavlovich'),
       (105, 'Yagunov',      'Feliks',   'Aleksandrovich'),
       (106, 'Lobachev',     'Semen',    'Evgenievich'),
       (107, 'Yushakova',    'Marianna', 'Klimentevna'),
       (108, 'Voloshin',     'Trofim',   'Fedorovich'),
       (109, 'Hitrovo',      'Tamara',   'Kirillovna'),
       (110, 'Ejler',        'Viktoriya','Konstantinovna')
;

INSERT INTO orders(id, time_of_created, time_of_begin, time_of_completion, price, status)
VALUES (101, '2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 500, 'COMPLETED'),
       (102, '2022-05-01 18:29:06', '2022-06-02 18:29:06', '2022-06-03 18:29:06', 400, 'IN_PROCESS'),
       (103, '2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 600, 'COMPLETED'),
       (104, '2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 800, 'IN_PROCESS'),
       (105, '2022-05-31 18:29:06', '2022-05-31 18:29:06', '2022-06-01 18:29:06', 900, 'CANCELLED')
;

INSERT INTO order_garage(order_id, garage_id, place)
VALUES (101, 101, 0),
       (102, 101, 1),
       (103, 101, 2),
       (104, 101, 3),
       (105, 102, 0)
;

INSERT INTO order_master(order_id, master_id)
VALUES (101, 101),
       (101, 102),
       (101, 103),
       (102, 104),
       (103, 105),
       (104, 106),
       (105, 107)
;