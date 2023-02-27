DROP SEQUENCE IF EXISTS Vegetable_seq;
CREATE SEQUENCE Vegetable_seq START with 1 increment by 1;

INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Potato');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Carrot');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Parsley');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Kohlrabi');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Leek');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Onion');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_seq'), 'Garlic');
