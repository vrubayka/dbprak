SELECT p.prod_id from product p
LEFT JOIN review r ON r.prod_id = p.prod_id
WHERE r.prod_id IS NULL;