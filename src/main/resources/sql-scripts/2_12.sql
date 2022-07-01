WITH leipzig_and_dresden AS (SELECT *
                             FROM product p
                             WHERE NOT EXISTS(
                                     SELECT *
                                     FROM store s
                                     WHERE p.prod_id
                                               NOT IN (
                                               SELECT prod_id
                                               FROM inventory
                                               WHERE store_id = s.store_id
                                                 AND price IS NOT NULL
                                           )
                                 )),
     leipzig_and_dresden_count AS (SELECT COUNT(*) AS product_count FROM leipzig_and_dresden),
     leipzig_cheapest AS (SELECT COUNT(*) AS leipzig_cheapest_count
                          FROM leipzig_and_dresden
                          WHERE (
                                        (SELECT MIN(price)
                                         FROM inventory i NATURAL JOIN store st
                                         WHERE i.prod_id = leipzig_and_dresden.prod_id AND i.store_id = st.store_id
                                           AND st.store_name = 'Leipzig')
                                        <=
                                        (SELECT MIN(price)
                                         FROM inventory i NATURAL JOIN store st
                                         WHERE i.prod_id = leipzig_and_dresden.prod_id AND i.store_id = st.store_id
                                           AND st.store_name != 'Leipzig')))
SELECT 100 * CAST(leipzig_cheapest.leipzig_cheapest_count AS float) / leipzig_and_dresden_count.product_count AS
           percentage
FROM leipzig_cheapest,
     leipzig_and_dresden_count;