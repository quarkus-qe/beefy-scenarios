insert into account (id, email) values (1, 'foo@bar.com');
insert into role (id, name) values  (1, 'admin');
insert into account_in_role (accountid, roleid) values (1, 1);
insert into customer (id, version, account_id) values (1,  1, 1);
insert into item (id, note, customerid) values (1, 'Item 1', 1);