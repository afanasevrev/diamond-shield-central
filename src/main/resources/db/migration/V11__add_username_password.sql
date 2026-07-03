INSERT INTO system_users(
    username,
    password_hash,
    email,
    full_name,
    is_active
)
VALUES (
    'admin',
    '$2a$10$Z3J3tGIEWe/5/7RbVd4o3.LYNj/mGFbS3pC/NCVNsztthYuNplndW',
    'admin@example.local',
    'System Administrator',
    true
)
ON CONFLICT (username) DO NOTHING;