CREATE OR REPLACE FUNCTION update_rating()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE product
    SET rating = (SELECT AVG(r.rating)
                  FROM ((SELECT * FROM product WHERE prod_id = NEW.prod_id) AS p
                      JOIN review AS r
                        ON p.prod_id = r.prod_id))
    WHERE prod_id = NEW.prod_id;
    RETURN NEW;
END
$$ LANGUAGE PLPGSQL
