INSERT INTO organizations(name, inn, description)
VALUES ('Default organization', NULL, 'Default organization for system administrator')
ON CONFLICT DO NOTHING;

INSERT INTO system_users(
    organization_id,
    username,
    password_hash,
    email,
    full_name,
    is_active
)
SELECT
    o.id,
    'admin',
    '$2a$10$Z3J3tGIEWe/5/7RbVd4o3.LYNj/mGFbS3pC/NCVNsztthYuNplndW',
    'admin@example.local',
    'System Administrator',
    true
FROM organizations o
WHERE o.name = 'Default organization'
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles(system_user_id, role_id, object_id)
SELECT
    u.id,
    r.id,
    NULL
FROM system_users u
JOIN roles r ON r.code = 'SYSTEM_ADMIN'
WHERE u.username = 'admin'
ON CONFLICT (system_user_id, role_id, object_id) DO NOTHING;