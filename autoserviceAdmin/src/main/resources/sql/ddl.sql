DROP TABLE IF EXISTS order_master;
DROP TABLE IF EXISTS order_garage;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS masters;
DROP TABLE IF EXISTS garages;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS role_authority;


CREATE SEQUENCE orders_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE masters_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE garages_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE order_master_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE order_garage_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE users_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE roles_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE authorities_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE user_role_id_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE role_authority_id_seq START WITH 100 INCREMENT BY 1;


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

CREATE TABLE users (
    id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('users_id_seq'),
    username  VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    enabled   BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE roles (
    id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('roles_id_seq'),
    name      VARCHAR(255) NOT NULL
    );

CREATE TABLE authorities (
    id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('authorities_id_seq'),
    name      VARCHAR(255) NOT NULL
    );

CREATE TABLE user_role (
    id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('user_role_id_seq'),
    user_id   INT8 NOT NULL,
    role_id   INT8 NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
    );

CREATE TABLE role_authority (
    id        INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('role_authority_id_seq'),
    role_id   INT8 NOT NULL,
    authority_id INT8 NOT NULL,

    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (authority_id) REFERENCES authorities (id)
    );


ALTER SEQUENCE orders_id_seq OWNED BY orders.id;
ALTER SEQUENCE masters_id_seq OWNED BY masters.id;
ALTER SEQUENCE garages_id_seq OWNED BY garages.id;
ALTER SEQUENCE order_master_id_seq OWNED BY order_master.id;
ALTER SEQUENCE order_garage_id_seq OWNED BY order_garage.id;
ALTER SEQUENCE users_id_seq OWNED BY users.id;
ALTER SEQUENCE roles_id_seq OWNED BY roles.id;
ALTER SEQUENCE authorities_id_seq OWNED BY authorities.id;
ALTER SEQUENCE user_role_id_seq OWNED BY user_role.id;
ALTER SEQUENCE role_authority_id_seq OWNED BY role_authority.id;