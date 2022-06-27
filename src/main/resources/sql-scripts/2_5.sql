SELECT prod_id
FROM review r
WHERE rating IN (1, 5)
GROUP BY prod_id
HAVING COUNT(DISTINCT r.rating) = 2;