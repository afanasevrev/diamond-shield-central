CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE organizations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    inn VARCHAR(20),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    is_active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE system_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID REFERENCES organizations(id),
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(30),
    full_name VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE user_roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    system_user_id UUID NOT NULL REFERENCES system_users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    object_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_user_role_object UNIQUE(system_user_id, role_id, object_id)
);

CREATE TABLE role_permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    CONSTRAINT uq_role_permission UNIQUE(role_id, permission_id)
);

CREATE TABLE audit_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    system_user_id UUID REFERENCES system_users(id),
    object_id UUID,
    action_type VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id UUID,
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_system_users_organization_id ON system_users(organization_id);
CREATE INDEX idx_user_roles_system_user_id ON user_roles(system_user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX idx_role_permissions_role_id ON role_permissions(role_id);
CREATE INDEX idx_role_permissions_permission_id ON role_permissions(permission_id);
CREATE INDEX idx_audit_log_system_user_created_at ON audit_log(system_user_id, created_at);
CREATE INDEX idx_audit_log_entity_type ON audit_log(entity_type);

INSERT INTO roles(code, name, description)
VALUES
('SYSTEM_ADMIN', 'Системный администратор', 'Полный доступ к системе'),
('ORG_ADMIN', 'Администратор организации', 'Администрирование организации'),
('OBJECT_ADMIN', 'Администратор объекта', 'Администрирование объекта'),
('OPERATOR', 'Оператор', 'Работа с журналами и событиями'),
('SECURITY_OFFICER', 'Сотрудник службы безопасности', 'Контроль безопасности');

INSERT INTO permissions(code, name, description)
VALUES
('ORGANIZATION_VIEW', 'Просмотр организаций', NULL),
('ORGANIZATION_MANAGE', 'Управление организациями', NULL),
('SYSTEM_USER_VIEW', 'Просмотр пользователей системы', NULL),
('SYSTEM_USER_MANAGE', 'Управление пользователями системы', NULL),
('ROLE_VIEW', 'Просмотр ролей', NULL),
('ROLE_MANAGE', 'Управление ролями', NULL),
('PERMISSION_VIEW', 'Просмотр прав', NULL),
('PERMISSION_MANAGE', 'Управление правами', NULL),
('AUDIT_VIEW', 'Просмотр аудита', NULL);

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SYSTEM_ADMIN';
