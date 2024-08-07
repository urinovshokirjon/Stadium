INSERT INTO profile (id, name, surname, phone, password, status, role,visible, create_date)
VALUES ('8cb1eb8e-bd2e-4413-b4d1-adb35255b121','Adminjon','Adminov','+998945022147','827ccbeea8a706c4c34a16891f84e7b','ACTIVE','ROLE_ADMIN',true,now()) ON CONFLICT (id) DO NOTHING;


