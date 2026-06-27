INSERT INTO permissions(code, name, description)
VALUES
('ACCESS_CHECK', 'Проверка доступа', 'Проверка доступа по идентификатору, точке прохода и расписанию')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;