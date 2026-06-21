INSERT INTO permissions(code, name, description)
VALUES
('PERSON_VIEW', 'Просмотр физических лиц', 'Просмотр сотрудников, жильцов, гостей и иных физических лиц'),
('PERSON_MANAGE', 'Управление физическими лицами', 'Создание, изменение и деактивация физических лиц'),
('PERSON_PHOTO_VIEW', 'Просмотр фотографий физических лиц', 'Просмотр фотографий пользователей доступа'),
('PERSON_PHOTO_MANAGE', 'Управление фотографиями физических лиц', 'Загрузка и изменение фотографий пользователей доступа'),
('IDENTIFIER_VIEW', 'Просмотр идентификаторов доступа', 'Просмотр карт, PIN-кодов, QR-кодов и иных идентификаторов'),
('IDENTIFIER_MANAGE', 'Управление идентификаторами доступа', 'Добавление, блокировка и деактивация идентификаторов'),
('PERSON_IMPORT', 'Импорт физических лиц', 'Групповое добавление физических лиц из XLSX-файла')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;