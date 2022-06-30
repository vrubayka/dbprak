(SELECT p.prod_id, p.prod_name, p.rating, 'DVD' as prod_group
 FROM product p
          JOIN dvd d on p.prod_id = d.dvd_id
 ORDER BY p.rating DESC, p.prod_name
 LIMIT 5)

UNION

(SELECT p.prod_id, p.prod_name, p.rating, 'CD' as prod_group
 FROM product p
          JOIN cd c on p.prod_id = c.cd_id
 ORDER BY p.rating DESC, p.prod_name
 LIMIT 5)

UNION

(SELECT p.prod_id, p.prod_name, p.rating, 'Book' as prod_group
 FROM product p
          JOIN book b on p.prod_id = b.book_id
 ORDER BY p.rating DESC, p.prod_name
 LIMIT 5)
ORDER BY prod_group, rating DESC, prod_name;