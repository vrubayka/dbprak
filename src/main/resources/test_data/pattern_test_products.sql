BEGIN ;
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test001', '%', 3.5);
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test002', '%test%', 3.5);
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test003', '%%', 3.5);
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test004', '\%%', 3.5);
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test004', '\%%_', 3.5);
INSERT INTO product (prod_id, prod_name, rating)
VALUES ('test005', '\%%_', 3.5);

COMMIT;