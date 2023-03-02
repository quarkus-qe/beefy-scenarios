ALTER SEQUENCE Vegetable_SEQ INCREMENT 1;
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Potato');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Carrot');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Parsley');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Kohlrabi');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Leek');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Onion');
INSERT INTO vegetable (id, name) VALUES (nextval('Vegetable_SEQ'), 'Garlic');
ALTER SEQUENCE Vegetable_SEQ RESTART with 57; -- 7 + pool size TODO: https://github.com/quarkusio/quarkus/issues/31481
