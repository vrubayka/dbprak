CREATE TRIGGER tg_rating
    AFTER INSERT ON review
    FOR EACH STATEMENT
    EXECUTE FUNCTION update_rating(NEW.prod_id);