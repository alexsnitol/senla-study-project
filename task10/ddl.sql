DROP TABLE IF EXISTS printer;
DROP TABLE IF EXISTS laptop;
DROP TABLE IF EXISTS pc;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    maker   VARCHAR(10)             NOT NULL,
    model   VARCHAR(50) PRIMARY KEY NOT NULL,
    type    VARCHAR(50)             NOT NULL
);

CREATE TABLE pc (
    code    INT PRIMARY KEY         NOT NULL,
    model   VARCHAR(50)             NOT NULL,
    speed   SMALLINT                NOT NULL,
    ram     SMALLINT                NOT NULL,
    hd      REAL                    NOT NULL,
    cd      VARCHAR(10)             NOT NULL,
    price   MONEY,

    FOREIGN KEY (model) REFERENCES product (model)
);

CREATE TABLE laptop (
    code    INT PRIMARY KEY         NOT NULL,
    model   VARCHAR(50)             NOT NULL,
    speed   SMALLINT                NOT NULL,
    ram     SMALLINT                NOT NULL,
    hd      REAL                    NOT NULL,
    screen  SMALLINT                NOT NULL,
    price   MONEY,

    FOREIGN KEY (model) REFERENCES product (model)
);

CREATE TABLE printer (
    code    INT PRIMARY KEY         NOT NULL,
    model   VARCHAR(50)             NOT NULL,
    color   CHAR(1)                 NOT NULL,
    type    VARCHAR(10)             NOT NULL,
    price   MONEY,

    FOREIGN KEY (model) REFERENCES product (model)
);