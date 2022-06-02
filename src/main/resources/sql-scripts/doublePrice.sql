SELECT i1.store_id AS i1store,
       i2.store_id AS i2store,
       i1.prod_id  AS prod_id,
       i1.price    AS i1price,
       i2.price    AS i2price
FROM inventory AS i1
         FULL JOIN inventory AS i2
                   ON i1.prod_id = i2.prod_id
WHERE (i1.price > (2 * i2.price) OR i2.price > (2 * i1.price))
  AND i1.store_id <> i2.store_id;