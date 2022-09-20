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

INSERT INTO users(username, password)
VALUES ('admin', '$2a$12$6bofBjw./veoSOd1YS8yN.ll5Xk4FOQL6OIOjoxmRRZPxvmJb4v/.'),
       ('user',  '$2a$12$nonCtCbG.eLIYzPaxtAZD.Yu1CRNOWPq5kfhSyFhKNLy5couQw6sa'),
       ('manager', '$2a$12$.vikqzGqyaPZqL7JQlOqMu1V4W8YZU/3.wsgx2R6JsltOTAY9yuXa')
;

INSERT INTO roles(name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER'),
       ('ROLE_MANAGER')
;

INSERT INTO user_role(user_id, role_id)
VALUES (100, 100),
       (101, 101),
       (102, 102)
;

INSERT INTO authorities(name)
VALUES ('ADD_AND_DELETE_FREE_PLACES'),
       ('SHIFT_TIME_OF_COMPLETION'),
       ('DELETE_ORDER')
;

INSERT INTO role_authority(role_id, authority_id)
VALUES (102, 100),
       (102, 101),
       (102, 102)
;