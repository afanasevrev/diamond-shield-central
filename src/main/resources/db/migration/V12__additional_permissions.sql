INSERT INTO permissions(code, name, description)
VALUES
('REALTIME_VIEW', 'Просмотр событий в реальном времени', NULL),
('LOCAL_MONITORING_VIEW', 'Просмотр локального АРМ', NULL),
('LOCAL_MANUAL_CONTROL', 'Ручное управление точками прохода', NULL),
('GUEST_REQUEST_VIEW', 'Просмотр гостевых заявок', NULL),
('GUEST_REQUEST_MANAGE', 'Согласование гостевых заявок', NULL),
('GUEST_VIEW', 'Просмотр гостей', NULL),
('GUEST_BLACKLIST_VIEW', 'Просмотр нежелательных гостей', NULL),
('GUEST_BLACKLIST_MANAGE', 'Управление нежелательными гостями', NULL)
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;