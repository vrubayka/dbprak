BEGIN;
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test', 'testProduct', 0);

INSERT INTO review (prod_id, username, reviewdate, rating)
VALUES ('test', 'testUser01', now(), 5);
INSERT INTO review (prod_id, username, reviewdate, rating)
VALUES ('test', 'testUser02', now(), 2);
INSERT INTO review (prod_id, username, reviewdate, rating)
VALUES ('test', 'testUser03', now(), 4);
INSERT INTO review (prod_id, username, reviewdate, rating)
VALUES ('test', 'testUser04', now(), 1);

COMMIT;