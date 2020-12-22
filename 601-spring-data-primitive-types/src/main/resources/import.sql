INSERT INTO address(id, zipCode) VALUES (1, '28080');

INSERT INTO book(bid, name, publicationYear, isbn, addressId) VALUES (1, 'Sapiens' , 2011, 9789295055025, 1);
INSERT INTO book(bid, name, publicationYear, isbn, addressId) VALUES (2, 'Homo Deus' , 2015, 9789295055026, 1);
INSERT INTO book(bid, name, publicationYear, isbn, addressId) VALUES (3, 'Enlightenment Now' , 2018, 9789295055027, 1);
INSERT INTO book(bid, name, publicationYear, isbn, addressId) VALUES (4, 'Factfulness' , 2018, 9789295055028, 1);
INSERT INTO book(bid, name, publicationYear, isbn, addressId) VALUES (5, 'Sleepwalkers' , 2012, 9789295055029, 1);
INSERT INTO book(bid, name, publicationYear, isbn, addressId) VALUES (6, 'The Silk Roads' , 2015, 9789295055020, 1);

INSERT INTO cat(id, color, breed, distinctive, birthDay, died, deathReason) VALUES (1, 'Grey', 'Scottish Fold', true, '2016-06-22 19:10:25-07', null, null);
INSERT INTO cat(id, color, breed, distinctive, birthDay, died, deathReason) VALUES (2, 'Grey', 'Persian', true, '2018-06-22 19:10:25-07', null, null);
INSERT INTO cat(id, color, breed, distinctive, birthDay, died, deathReason) VALUES (3, 'White', 'Turkish Angora', true, '2019-06-22 19:10:25-07', null, null);
INSERT INTO cat(id, color, breed, distinctive, birthDay, died, deathReason) VALUES (4, null , 'British Shorthair', false, '2020-01-22 19:10:25-07', '2020-01-22 19:10:25-07', 'covid19');
INSERT INTO cat(id, color, breed, distinctive, birthDay, died, deathReason) VALUES (5, 'Black' , 'Bombay Cat', true, '2020-02-22 19:10:25-07', '2020-03-22 19:10:25-07', 'covid19');
