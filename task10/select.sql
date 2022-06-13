-- OPT 1
-- Найти номер модели, скорость и размер жесткого диска для всех ПК стоимостью менее 500 долларов.
SELECT
    model, speed, hd
FROM
    pc
WHERE
    price < 500::money
;

-- OPT 2
-- Найти производителей принтеров. Вывести поля: maker.
SELECT
    maker
FROM
    product
WHERE
    type = 'Printer'
GROUP BY
    maker
;

-- OPT 3
-- Найти номер модели, объем памяти и размеры экранов ноутбуков, цена которых превышает 1000 долларов.
SELECT
    model, ram, screen
FROM
    laptop
WHERE
    price > 1000::money
;

-- OPT 4
-- Найти все записи таблицы Printer для цветных принтеров.
SELECT
    *
FROM
    printer
WHERE
    color = 'y'
;

-- OPT 5
-- Найти номер модели, скорость и размер жесткого диска для ПК, имеющих скорость cd 12x или 24x и цену менее 600 долларов.
SELECT
    model, speed, hd
FROM
    pc
WHERE
    (cd = '12x' OR cd = '24x')
    AND price < 600::money
;

-- OPT 6
-- Указать производителя и скорость для тех ноутбуков, которые имеют жесткий диск объемом не менее 100 Гбайт.
SELECT
    product.maker, laptop.speed
FROM
    product
    INNER JOIN laptop ON product.model = laptop.model
WHERE
    hd > 100
;

-- OPT 7
-- Найти номера моделей и цены всех продуктов (любого типа), выпущенных производителем B (латинская буква).
SELECT
    product.model,
    COALESCE (l.price, pc.price, pr.price) AS price
FROM
    product
    FULL JOIN laptop l      ON product.model = l.model
    FULL JOIN pc            ON product.model = pc.model
    FULL JOIN printer pr    ON product.model = pr.model
WHERE product.maker LIKE 'B%';

-- OPT 8
-- Найти производителя, выпускающего ПК, но не ноутбуки.
SELECT
    product.maker
FROM
    product
WHERE product.type = 'Laptop'
   OR product.type = 'PC'
GROUP BY
    product.maker
EXCEPT
SELECT
    product.maker
FROM
    product
WHERE product.type = 'Laptop'
GROUP BY
    product.maker
;

-- OPT 9
-- Найти производителей ПК с процессором не менее 450 Мгц. Вывести поля: maker.
SELECT
    product.maker
FROM
    product
    INNER JOIN pc ON product.model = pc.model
WHERE
    pc.speed >= 450
GROUP BY
    product.maker
;


-- OPT 10
-- Найти принтеры, имеющие самую высокую цену. Вывести поля: model, price.
SELECT
    printer.model, printer.price
FROM
    printer
    RIGHT JOIN (
        SELECT MAX(printer.price) AS max
        FROM printer) AS q ON printer.price = q.max
;


-- OPT 11
-- Найти среднюю скорость ПК.
SELECT
    AVG(speed)
FROM
    pc
;

-- OPT 12
-- Найти среднюю скорость ноутбуков, цена которых превышает 1000 долларов.
SELECT
    AVG(speed)
FROM
    laptop
WHERE
    laptop.price > 1000::money
;


-- OPT 13
-- Найти среднюю скорость ПК, выпущенных производителем A.
SELECT
    AVG(l.speed)
FROM
    product
    INNER JOIN laptop l ON product.model = l.model
WHERE
    product.maker = 'A'
;


-- OPT 14
-- Для каждого значения скорости процессора найти среднюю стоимость ПК с такой же скоростью. Вывести поля: скорость, средняя цена.
SELECT
    speed, AVG(price::DECIMAL)
FROM
    pc
GROUP BY
    speed
;


-- OPT 15
-- Найти размеры жестких дисков, совпадающих у двух и более PC. Вывести поля: hd.
SELECT
    hd
FROM
    pc
GROUP BY
    hd
HAVING
    COUNT(hd) >= 2
;


-- OPT 16
-- Найти пары моделей PC, имеющих одинаковые скорость процессора и RAM. В результате каждая пара указывается только один раз, т.е. (i,j), но не (j,i), Порядок вывода полей: модель с большим номером, модель с меньшим номером, скорость, RAM.
SELECT
    pc1.model AS model, pc2.model AS same_model, pc1.speed, pc1.ram
FROM
    pc AS pc1
    INNER JOIN (
        SELECT
            code, model, speed, ram
        FROM
            pc
    ) AS pc2 ON pc1.speed = pc2.speed AND pc1.ram = pc2.ram AND pc1.model != pc2.model
WHERE
    pc1.code > pc2.code;


-- OPT 17
-- Найти модели ноутбуков, скорость которых меньше скорости любого из ПК. Вывести поля: type, model, speed.
SELECT
    type, l.model, l.speed
FROM
    product
    INNER JOIN laptop l on product.model = l.model
    INNER JOIN (
        SELECT
            MIN(speed) AS min_speed
        FROM
            pc
    ) AS q ON l.speed < q.min_speed
;


-- OPT 18
-- Найти производителей самых дешевых цветных принтеров. Вывести поля: maker, price.
SELECT
    maker, price
FROM
    product
    INNER JOIN printer p on product.model = p.model
    INNER JOIN (
        SELECT
            MIN(price) AS min_price
        FROM
            printer
        WHERE
            color = 'y'
    ) AS q ON p.price = q.min_price
;


-- OPT 19
-- Для каждого производителя найти средний размер экрана выпускаемых им ноутбуков. Вывести поля: maker, средний размер экрана.
SELECT
    maker, AVG(screen)
FROM
    product
    INNER JOIN laptop l ON product.model = l.model
GROUP BY
    maker
;


-- OPT 20
-- Найти производителей, выпускающих по меньшей мере три различных модели ПК. Вывести поля: maker, число моделей.
SELECT
    maker, COUNT(pc.model)
FROM
    product
    INNER JOIN pc ON product.model = pc.model
GROUP BY
    maker
HAVING
    COUNT(pc.model) >= 3
;


-- OPT 21
-- Найти максимальную цену ПК, выпускаемых каждым производителем. Вывести поля: maker, максимальная цена.
SELECT
    maker, MAX(pc.price)
FROM
    product
    INNER JOIN pc ON product.model = pc.model
GROUP BY
    maker
;


-- OPT 22
-- Для каждого значения скорости процессора ПК, превышающего 600 МГц, найти среднюю цену ПК с такой же скоростью. Вывести поля: speed, средняя цена.
SELECT
    speed, AVG(price::DECIMAL)
FROM
    pc
WHERE
    speed > 600
GROUP BY
    speed
;


-- OPT 23
-- Найти производителей, которые производили бы как ПК, так и ноутбуки со скоростью не менее 750 МГц. Вывести поля: maker
SELECT
    product.maker
FROM
    product
    INNER JOIN laptop l on product.model = l.model
    INNER JOIN (
        SELECT
            maker
        FROM
            product
            INNER JOIN pc p on product.model = p.model
        WHERE
            speed > 750
    ) AS pc ON product.maker = pc.maker
WHERE
    l.speed > 750
GROUP BY
    product.maker
;


-- OPT 24
-- Перечислить номера моделей любых типов, имеющих самую высокую цену по всей имеющейся в базе данных продукции.

-- как я понял, надо отобразить самые дорогие модели по категориям; если же надо было отобразить самый дорогой товар, то тогда можно просто либо из моего запроса взять максимальную цену, либо фулл джоинами вывести все товары и от туда взять макс цену
SELECT
    product.model
FROM
    product
    FULL JOIN laptop l on product.model = l.model
    FULL JOIN (
        SELECT
            MAX(price) AS max_laptop_price
        FROM
            laptop
    ) AS ql ON l.price = ql.max_laptop_price
    FULL JOIN pc ON product.model = pc.model
    FULL JOIN (
        SELECT
            MAX(price) AS max_pc_price
        FROM
            pc
    ) AS qpc ON pc.price = qpc.max_pc_price
    FULL JOIN printer p ON product.model = p.model
    FULL JOIN (
        SELECT
            MAX(price) AS max_printer_price
        FROM
            printer
    ) AS qp ON p.price = qp.max_printer_price
WHERE
    max_printer_price IS NOT NULL
    OR max_pc_price IS NOT NULL
    OR max_laptop_price IS NOT NULL
;


-- OPT 25
-- Найти производителей принтеров, которые производят ПК с наименьшим объемом RAM и с самым быстрым процессором среди всех ПК, имеющих наименьший объем RAM. Вывести поля: maker

SELECT
    product.maker
FROM
    product
    INNER JOIN printer p on product.model = p.model
    INNER JOIN (
        SELECT
            maker
        FROM
            product
            INNER JOIN pc ON product.model = pc.model
            INNER JOIN (
                SELECT
                    MIN(ram) AS min_ram
                FROM
                    pc
            ) AS pc_min_ram ON pc.ram = pc_min_ram.min_ram
            INNER JOIN (
                SELECT
                    MAX(speed) AS max_speed
                FROM
                    pc
                    INNER JOIN (
                        SELECT
                            MIN(ram) AS min_ram
                        FROM
                            pc
                    ) AS pc_min_ram ON pc.ram = pc_min_ram.min_ram
            ) AS pc_max_speed ON pc.speed = pc_max_speed.max_speed
    ) AS pc ON product.maker = pc.maker
;
