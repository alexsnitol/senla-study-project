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