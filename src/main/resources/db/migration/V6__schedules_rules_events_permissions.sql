INSERT INTO permissions(code, name, description)
VALUES
('SCHEDULE_VIEW', 'Просмотр расписаний', 'Просмотр расписаний доступа'),
('SCHEDULE_MANAGE', 'Управление расписаниями', 'Создание и изменение расписаний доступа'),
('ACCESS_RULE_VIEW', 'Просмотр правил доступа', 'Просмотр правил доступа физических лиц'),
('ACCESS_RULE_MANAGE', 'Управление правилами доступа', 'Создание и изменение правил доступа'),
('ACCESS_EVENT_VIEW', 'Просмотр событий доступа', 'Просмотр журнала проходов и отказов'),
('ALARM_EVENT_VIEW', 'Просмотр тревожных событий', 'Просмотр журнала тревог'),
('ALARM_EVENT_MANAGE', 'Обработка тревожных событий', 'Подтверждение и закрытие тревожных событий')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;