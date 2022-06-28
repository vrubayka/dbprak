SELECT prod_id FROM product NATURAL JOIN inventory
               WHERE product.prod_id = inventory.prod_id AND inventory.price IS NULL;