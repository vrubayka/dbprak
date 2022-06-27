CREATE FUNCTION update_rating()
    RETURN TRIGGER
    LANGUAGE PLPGSQL
AS $$
    BEGIN
    UPDATE product
    SET rating
    END
$$
