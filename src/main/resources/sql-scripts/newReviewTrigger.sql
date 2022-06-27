BEGIN;

DROP TRIGGER IF EXISTS tg_rating ON review;

CREATE TRIGGER tg_rating
    AFTER INSERT ON review
    FOR EACH ROW
    EXECUTE FUNCTION update_rating();

COMMIT;