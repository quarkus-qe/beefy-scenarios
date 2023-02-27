--CREATE SEQUENCE fruit_SEQ START with 8 increment by 1;

--DROP SEQUENCE IF EXISTS fruit_SEQ;
--CREATE SEQUENCE fruit_SEQ START with 8 increment by 1;

--INSERT INTO fruit (id, name) VALUES (nextval('fruit_SEQ'), 'Apple');

DROP SEQUENCE IF EXISTS rs_fruit_sequence;
CREATE SEQUENCE rs_fruit_sequence START 1 increment by 1;

INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Apple');
INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Pear');
INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Strawberry');
INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Plum');
INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Cherry');
INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Berry');
INSERT INTO fruit (id, name) VALUES (nextval('rs_fruit_sequence'), 'Cranberry');
--INSERT INTO fruit (id, name) VALUES (1, 'Apple');
--INSERT INTO fruit (id, name) VALUES (2, 'Pear');
--INSERT INTO fruit (id, name) VALUES (3, 'Strawberry');
--INSERT INTO fruit (id, name) VALUES (4, 'Plum');
--INSERT INTO fruit (id, name) VALUES (5, 'Cherry');
--INSERT INTO fruit (id, name) VALUES (6, 'Berry');
--INSERT INTO fruit (id, name) VALUES (7, 'Cranberry');