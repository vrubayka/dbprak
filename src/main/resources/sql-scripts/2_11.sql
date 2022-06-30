SELECT prod_id
FROM product p
WHERE NOT EXISTS(
        SELECT *
        FROM store s
        WHERE p.prod_id
                  NOT IN (SELECT prod_id
                          FROM inventory
                          WHERE store_id = s.store_id
                            AND price IS NOT NULL)
    );