INSERT INTO permissions(code, name, description)
VALUES
('SYNC_VIEW', 'Просмотр синхронизации', 'Просмотр истории синхронизаций локальных серверов'),
('SYNC_MANAGE', 'Управление синхронизацией', 'Управление параметрами синхронизации локальных серверов')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;