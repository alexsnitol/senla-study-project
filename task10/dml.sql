INSERT INTO product(maker, model, type)
VALUES ('Lenovo', 'ThinkCentre M70q', 'PC'),
       ('Lenovo', 'IdeaCentre Mini 5', 'PC'),
       ('Lenovo', '90Q7000QRS', 'PC'),
       ('BHP', 'Pavilion TP01-2074ur', 'PC'),
       ('BHP', 'Pavilion Gaming TG01-2013ur', 'PC'),
       ('Acer', 'Veriton X2665G', 'PC'),
       ('Acer', 'Veriton X2665GF', 'PC'),
       ('SlowPC', 'Model PC1', 'PC'),
       ('BHP', 'Slow PC', 'PC'),

       ('DELL', 'G3', 'Laptop'),
       ('BHP', '17-ca3008ur', 'Laptop'),
       ('BHP', 'Victus 16-d0052ur', 'Laptop'),
       ('A', 'Summit E16', 'Laptop'),
       ('A', 'Summit E17', 'Laptop'),
       ('Lenovo', 'IdeaPad 5 Pro', 'Laptop'),
       ('SlowPC', 'Model L1', 'Laptop'),

       ('BHP', 'Color LaserJet Pro M479fnw', 'Printer'),
       ('Canon', 'i-SENSYS MF742Cdw', 'Printer'),
       ('XEROX', 'C235', 'Printer'),
       ('Kyocera', 'Ecosys M5526cdw', 'Printer'),
       ('Kyocera', 'Ecosys M5521cdw', 'Printer'),
       ('Kyocera', 'Ecosys M5521cdwF', 'Printer'),
       ('SlowPC', 'Model P1', 'Printer');


INSERT INTO pc(code, model, speed, ram, hd, cd, price)
VALUES (0, 'ThinkCentre M70q', 3500, 8192, 256, '12x', 450),
       (1, 'IdeaCentre Mini 5', 3800, 8192, 256, '24x', 520),
       (2, '90Q7000QRS', 3600, 8192, 256, '16x', 1010),
       (3, 'Pavilion TP01-2074ur', 4400, 8192, 256, '12x', 1665),
       (4, 'Pavilion Gaming TG01-2013ur', 4400, 16384, 512, '24x', 2071),
       (5, 'Veriton X2665G', 4200, 8192, 1024, '12x', 813),
       (6, 'Veriton X2665GF', 4200, 8192, 1024, '12x', 820),
       (7, 'Model PC1', 550, 1024, 128, '4x', 100),
       (8, 'Slow PC', 650, 1024, 128, '4x', 100);

INSERT INTO laptop(code, model, speed, ram, hd, screen, price)
VALUES (0, 'G3', 3400, 8192, 1024, 15, 1212),
       (1, '17-ca3008ur', 4000, 8192, 1024, 17, 1325),
       (2, 'Victus 16-d0052ur', 4700, 16384, 512, 16, 2060),
       (3, 'Summit E16', 5000, 16384, 1024, 16, 3157),
       (4, 'Summit E17', 5400, 16384, 2048, 16, 4557),
       (5, 'IdeaPad 5 Pro', 3500, 8192, 256, 15, 605),
       (6, 'Model L1', 550, 1024, 128, 15, 105);

INSERT INTO printer(code, model, color, type, price)
VALUES (0, 'Color LaserJet Pro M479fnw', 'y', 'Laser', 2075),
       (1, 'i-SENSYS MF742Cdw', 'y', 'Matrix', 2031),
       (2, 'C235', 'n', 'Laser', 1184),
       (3, 'Ecosys M5526cdw', 'y', 'Jet', 2108),
       (4, 'Ecosys M5521cdw', 'n', 'Jet', 1905),
       (5, 'Ecosys M5521cdwF', 'y', 'Jet', 1955),
       (6, 'Model P1', 'n', 'Laser', 100);