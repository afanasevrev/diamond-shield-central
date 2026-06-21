INSERT INTO permissions(code, name, description)
VALUES
('OBJECT_VIEW', 'Просмотр объектов', 'Просмотр объектов СКУД'),
('OBJECT_MANAGE', 'Управление объектами', 'Создание и изменение объектов СКУД'),
('ZONE_VIEW', 'Просмотр зон доступа', 'Просмотр зон доступа объектов'),
('ZONE_MANAGE', 'Управление зонами доступа', 'Создание и изменение зон доступа'),
('LOCAL_SERVER_VIEW', 'Просмотр локальных серверов', 'Просмотр локальных серверов объектов'),
('LOCAL_SERVER_MANAGE', 'Управление локальными серверами', 'Создание и изменение локальных серверов'),
('EQUIPMENT_VIEW', 'Просмотр оборудования', 'Просмотр контроллеров, считывателей и точек прохода'),
('EQUIPMENT_MANAGE', 'Управление оборудованием', 'Создание и изменение оборудования'),
('DEVICE_STATUS_VIEW', 'Просмотр состояния оборудования', 'Просмотр истории состояния оборудования')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN'
  AND p.code IN (
    'OBJECT_VIEW',
    'OBJECT_MANAGE',
    'ZONE_VIEW',
    'ZONE_MANAGE',
    'LOCAL_SERVER_VIEW',
    'LOCAL_SERVER_MANAGE',
    'EQUIPMENT_VIEW',
    'EQUIPMENT_MANAGE',
    'DEVICE_STATUS_VIEW'
  )
ON CONFLICT DO NOTHING;