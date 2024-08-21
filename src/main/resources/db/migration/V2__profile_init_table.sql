INSERT INTO profile (id, name, surname, phone, password, status, role,visible, create_date)
VALUES
       ('8cb1eb8e-bd2e-4413-b4d1-adb35255b121','Adminjon','Adminov','+998945022147','827ccbeea8a706c4c34a16891f84e7b','ACTIVE','ROLE_ADMIN',true,now()),
       ('8cb1eb8e-bd2e-4413-b4d1-adb35255b122','Ownerjon','Ownerov','+998945022148','827ccbeea8a706c4c34a16891f84e7b','ACTIVE','ROLE_OWNER',true,now()),
       ('8cb1eb8e-bd2e-4413-b4d1-adb35255b123','Userjon','Userov','+998945022149','827ccbeea8a706c4c34a16891f84e7b','ACTIVE','ROLE_USER',true,now());
SELECT setval('region_id_seq', max(id)) FROM region;


