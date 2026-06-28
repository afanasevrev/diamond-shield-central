CREATE TABLE objects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    name VARCHAR(255) NOT NULL,
    object_type VARCHAR(100),
    address TEXT,
    timezone VARCHAR(50),
    work_schedule_id UUID,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE object_zones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID NOT NULL REFERENCES objects(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    zone_type VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE local_servers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID NOT NULL REFERENCES objects(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    ip_address VARCHAR(50),
    mac_address VARCHAR(50),
    server_token_hash TEXT,
    software_version VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'offline',
    last_seen_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE controllers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID NOT NULL REFERENCES objects(id) ON DELETE CASCADE,
    local_server_id UUID REFERENCES local_servers(id),
    name VARCHAR(255) NOT NULL,
    model VARCHAR(100),
    serial_number VARCHAR(100),
    ip_address VARCHAR(50),
    port INTEGER,
    status VARCHAR(50) NOT NULL DEFAULT 'offline',
    last_seen_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE readers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    controller_id UUID NOT NULL REFERENCES controllers(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    reader_type VARCHAR(50),
    direction VARCHAR(20),
    status VARCHAR(50) NOT NULL DEFAULT 'offline',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE access_points (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID NOT NULL REFERENCES objects(id) ON DELETE CASCADE,
    zone_from_id UUID REFERENCES object_zones(id),
    zone_to_id UUID REFERENCES object_zones(id),
    controller_id UUID REFERENCES controllers(id),
    name VARCHAR(255) NOT NULL,
    access_point_type VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'offline',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE device_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID REFERENCES objects(id),
    device_type VARCHAR(50) NOT NULL,
    device_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_objects_organization_id ON objects(organization_id);
CREATE INDEX idx_objects_is_active ON objects(is_active);

CREATE INDEX idx_object_zones_object_id ON object_zones(object_id);

CREATE INDEX idx_local_servers_object_id ON local_servers(object_id);
CREATE INDEX idx_local_servers_status ON local_servers(status);

CREATE INDEX idx_controllers_object_id ON controllers(object_id);
CREATE INDEX idx_controllers_local_server_id ON controllers(local_server_id);
CREATE INDEX idx_controllers_status ON controllers(status);

CREATE INDEX idx_readers_controller_id ON readers(controller_id);
CREATE INDEX idx_readers_status ON readers(status);

CREATE INDEX idx_access_points_object_id ON access_points(object_id);
CREATE INDEX idx_access_points_controller_id ON access_points(controller_id);
CREATE INDEX idx_access_points_status ON access_points(status);
CREATE INDEX idx_access_points_is_active ON access_points(is_active);

CREATE INDEX idx_device_status_history_object_id ON device_status_history(object_id);
CREATE INDEX idx_device_status_history_device_type_device_id ON device_status_history(device_type, device_id);
CREATE INDEX idx_device_status_history_created_at ON device_status_history(created_at);

ALTER TABLE readers
ADD COLUMN IF NOT EXISTS access_point_id UUID;

ALTER TABLE readers
ADD CONSTRAINT fk_readers_access_point
FOREIGN KEY (access_point_id)
REFERENCES access_points(id)
ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_readers_access_point_id
ON readers(access_point_id);
