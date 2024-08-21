INSERT INTO region(id, name_uz, name_en, name_ru, name_kr, created_date, visible)
VALUES
    (1, 'Toshkent shahri', 'Tashkent city', 'Город Ташкент', 'Ташкент шаҳри', now(), true),
    (2, 'Toshkent viloyati', 'Tashkent region', 'Ташкентская область', 'Тошкент вилояти', now(), true),
    (3, 'Andijon viloyati', 'Andijan region', 'Андижанская область', 'Андижон вилояти', now(), true),
    (4, 'Buxoro viloyati', 'Bukhara region', 'Бухарская область', 'Бухоро вилояти', now(), true),
    (5, 'Jizzax viloyati', 'Jizzakh region', 'Джизакская область', 'Жиззах вилояти', now(), true),
    (6, 'Qoraqalpog‘iston', 'Karakalpakstan', 'Каракалпакстан', 'Қорақалпоғистон', now(), true),
    (7, 'Qashqadaryo viloyati', 'Kashkadarya region', 'Кашкадарьинская область', 'Қашқадарё вилояти', now(), true),
    (8, 'Navoiy viloyati', 'Navoi region', 'Навоийская область', 'Навоий вилояти', now(), true),
    (9, 'Namangan viloyati', 'Namangan region', 'Наманганская область', 'Наманган вилояти', now(), true),
    (10, 'Samarqand viloyati', 'Samarkand region', 'Самаркандская область', 'Самарқанд вилояти', now(), true),
    (11, 'Surxondaryo viloyati', 'Surkhandarya region', 'Сурхандарьинская область', 'Сурхондарё вилояти', now(), true),
    (12, 'Sirdaryo viloyati', 'Syrdarya region', 'Сырдарьинская область', 'Сырдарё вилояти', now(), true),
    (13, 'Farg‘ona viloyati', 'Fergana region', 'Ферганская область', 'Фарғона вилояти', now(), true),
    (14, 'Xorazm viloyati', 'Khorezm region', 'Хорезмская область', 'Хоразм вилояти', now(), true);
SELECT setval('region_id_seq', max(id)) FROM region;

