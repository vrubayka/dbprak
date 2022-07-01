WITH RECURSIVE cat_parent AS (SELECT category_id, category_name, category_id AS parent
                              FROM category
                              WHERE super_category IS NULL

                              UNION ALL

                              SELECT c.category_id, c.category_name, cat_parent.parent
                              FROM cat_parent,
                                   category AS c
                              WHERE c.super_category = cat_parent.category_id)

SELECT DISTINCT prod.prod_id AS prod_id
FROM (similar_products AS prod
    JOIN (SELECT pc.prod_id AS prod_id, par.parent AS main_category
          FROM cat_parent AS par
                   JOIN product_category AS pc
                        ON par.category_id = pc.category_id) AS main ON main.prod_id = prod.prod_id
    JOIN (SELECT pc.prod_id AS prod_id, par.parent AS sim_main_category
          FROM cat_parent AS par
                   JOIN product_category AS pc
                        ON par.category_id = pc.category_id) AS sim_main
      ON sim_main.prod_id = prod.similar_prod_id)
WHERE main_category <> sim_main_category;
