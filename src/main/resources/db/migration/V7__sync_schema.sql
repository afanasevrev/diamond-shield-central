ALTER TABLE local_servers
ADD COLUMN sync_enabled BOOLEAN NOT NULL DEFAULT true;

ALTER TABLE local_servers
ADD COLUMN last_sync_at TIMESTAMP;

CREATE TABLE sync_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    local_server_id UUID NOT NULL REFERENCES local_servers(id) ON DELETE CASCADE,
    object_id UUID REFERENCES objects(id) ON DELETE SET NULL,

    sync_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,

    requested_at TIMESTAMP NOT NULL DEFAULT now(),
    completed_at TIMESTAMP,

    total_items INTEGER NOT NULL DEFAULT 0,
    accepted_items INTEGER NOT NULL DEFAULT 0,
    skipped_items INTEGER NOT NULL DEFAULT 0,
    error_items INTEGER NOT NULL DEFAULT 0,

    error_message TEXT
);

CREATE INDEX idx_sync_history_local_server_id ON sync_history(local_server_id);
CREATE INDEX idx_sync_history_object_id ON sync_history(object_id);
CREATE INDEX idx_sync_history_status ON sync_history(status);
CREATE INDEX idx_sync_history_requested_at ON sync_history(requested_at);

CREATE INDEX idx_local_servers_sync_enabled ON local_servers(sync_enabled);
CREATE INDEX idx_local_servers_last_sync_at ON local_servers(last_sync_at);