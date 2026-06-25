CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    organization_id UUID NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,

    name VARCHAR(255) NOT NULL,
    description TEXT,

    is_active BOOLEAN NOT NULL DEFAULT true,

    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE schedule_intervals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    schedule_id UUID NOT NULL REFERENCES schedules(id) ON DELETE CASCADE,

    day_of_week INTEGER NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT chk_schedule_interval_day CHECK (day_of_week BETWEEN 1 AND 7),
    CONSTRAINT chk_schedule_interval_time CHECK (start_time < end_time)
);

ALTER TABLE objects
ADD CONSTRAINT fk_objects_work_schedule
FOREIGN KEY (work_schedule_id)
REFERENCES schedules(id)
ON DELETE SET NULL;

CREATE TABLE access_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    access_point_id UUID NOT NULL REFERENCES access_points(id) ON DELETE CASCADE,
    schedule_id UUID REFERENCES schedules(id) ON DELETE SET NULL,

    valid_from TIMESTAMP,
    valid_to TIMESTAMP,

    is_active BOOLEAN NOT NULL DEFAULT true,

    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_access_rule_person_point UNIQUE(person_id, access_point_id)
);

CREATE TABLE access_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    local_server_id UUID REFERENCES local_servers(id) ON DELETE SET NULL,
    local_event_id VARCHAR(100),

    object_id UUID REFERENCES objects(id) ON DELETE SET NULL,
    access_point_id UUID REFERENCES access_points(id) ON DELETE SET NULL,
    reader_id UUID REFERENCES readers(id) ON DELETE SET NULL,
    controller_id UUID REFERENCES controllers(id) ON DELETE SET NULL,

    person_id UUID REFERENCES persons(id) ON DELETE SET NULL,
    identifier_id UUID REFERENCES access_identifiers(id) ON DELETE SET NULL,

    event_time TIMESTAMP NOT NULL,
    direction VARCHAR(20),

    access_result VARCHAR(50) NOT NULL,
    reason VARCHAR(255),

    identifier_type VARCHAR(50),
    identifier_masked VARCHAR(100),
    identifier_value_hash TEXT,

    is_unknown_identifier BOOLEAN NOT NULL DEFAULT false,

    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_access_events_local UNIQUE(local_server_id, local_event_id)
);

CREATE TABLE alarm_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    local_server_id UUID REFERENCES local_servers(id) ON DELETE SET NULL,
    local_event_id VARCHAR(100),

    object_id UUID REFERENCES objects(id) ON DELETE SET NULL,
    access_point_id UUID REFERENCES access_points(id) ON DELETE SET NULL,
    reader_id UUID REFERENCES readers(id) ON DELETE SET NULL,
    controller_id UUID REFERENCES controllers(id) ON DELETE SET NULL,

    alarm_type VARCHAR(100) NOT NULL,
    severity VARCHAR(50) NOT NULL,

    message TEXT,

    status VARCHAR(50) NOT NULL DEFAULT 'new',

    occurred_at TIMESTAMP NOT NULL,
    acknowledged_at TIMESTAMP,
    resolved_at TIMESTAMP,

    acknowledged_by UUID REFERENCES system_users(id) ON DELETE SET NULL,
    resolved_by UUID REFERENCES system_users(id) ON DELETE SET NULL,

    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_alarm_events_local UNIQUE(local_server_id, local_event_id)
);

CREATE INDEX idx_schedules_organization_id ON schedules(organization_id);
CREATE INDEX idx_schedules_is_active ON schedules(is_active);

CREATE INDEX idx_schedule_intervals_schedule_id ON schedule_intervals(schedule_id);

CREATE INDEX idx_access_rules_person_id ON access_rules(person_id);
CREATE INDEX idx_access_rules_access_point_id ON access_rules(access_point_id);
CREATE INDEX idx_access_rules_schedule_id ON access_rules(schedule_id);
CREATE INDEX idx_access_rules_is_active ON access_rules(is_active);

CREATE INDEX idx_access_events_event_time ON access_events(event_time);
CREATE INDEX idx_access_events_object_id ON access_events(object_id);
CREATE INDEX idx_access_events_access_point_id ON access_events(access_point_id);
CREATE INDEX idx_access_events_person_id ON access_events(person_id);
CREATE INDEX idx_access_events_identifier_id ON access_events(identifier_id);
CREATE INDEX idx_access_events_result ON access_events(access_result);
CREATE INDEX idx_access_events_unknown ON access_events(is_unknown_identifier);
CREATE INDEX idx_access_events_local_event ON access_events(local_server_id, local_event_id);

CREATE INDEX idx_alarm_events_occurred_at ON alarm_events(occurred_at);
CREATE INDEX idx_alarm_events_object_id ON alarm_events(object_id);
CREATE INDEX idx_alarm_events_alarm_type ON alarm_events(alarm_type);
CREATE INDEX idx_alarm_events_severity ON alarm_events(severity);
CREATE INDEX idx_alarm_events_status ON alarm_events(status);
CREATE INDEX idx_alarm_events_local_event ON alarm_events(local_server_id, local_event_id);