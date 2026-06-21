CREATE TABLE persons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    organization_id UUID NOT NULL REFERENCES organizations(id),

    person_type VARCHAR(50) NOT NULL,
    personnel_number VARCHAR(50),

    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),

    birth_date DATE,

    phone VARCHAR(30),
    email VARCHAR(255),

    document_type VARCHAR(100),
    document_series VARCHAR(20),
    document_number VARCHAR(30),

    is_active BOOLEAN NOT NULL DEFAULT true,

    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE person_photos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,

    file_name VARCHAR(255),
    content_type VARCHAR(100),
    file_size INTEGER NOT NULL,
    photo_data BYTEA NOT NULL,

    is_main BOOLEAN NOT NULL DEFAULT false,

    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT chk_person_photo_size CHECK (file_size <= 102400)
);

CREATE TABLE access_identifiers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,

    identifier_type VARCHAR(50) NOT NULL,
    identifier_value_hash TEXT NOT NULL,
    identifier_masked VARCHAR(100),

    valid_from TIMESTAMP,
    valid_to TIMESTAMP,

    status VARCHAR(50) NOT NULL DEFAULT 'active',

    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),

    reader_id UUID REFERENCES readers(id) ON DELETE SET NULL,
    issued_by UUID REFERENCES system_users(id) ON DELETE SET NULL,
    issued_at TIMESTAMP,

    comment TEXT,

    CONSTRAINT uq_access_identifier_type_hash UNIQUE(identifier_type, identifier_value_hash)
);

CREATE TABLE import_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    organization_id UUID NOT NULL REFERENCES organizations(id),
    system_user_id UUID REFERENCES system_users(id) ON DELETE SET NULL,

    file_name VARCHAR(255),
    import_type VARCHAR(100) NOT NULL,

    total_rows INTEGER NOT NULL DEFAULT 0,
    success_rows INTEGER NOT NULL DEFAULT 0,
    skipped_rows INTEGER NOT NULL DEFAULT 0,

    status VARCHAR(50) NOT NULL,

    error_message TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE import_history_details (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    import_history_id UUID NOT NULL REFERENCES import_history(id) ON DELETE CASCADE,

    row_number INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason TEXT,

    raw_data JSONB,

    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_persons_organization_type_last_name
    ON persons(organization_id, person_type, last_name);

CREATE UNIQUE INDEX uq_persons_org_personnel_number_not_null
    ON persons(organization_id, personnel_number)
    WHERE personnel_number IS NOT NULL;

CREATE INDEX idx_access_identifiers_identifier_value_hash
    ON access_identifiers(identifier_value_hash);

CREATE INDEX idx_access_identifiers_person_id
    ON access_identifiers(person_id);

CREATE INDEX idx_access_identifiers_status
    ON access_identifiers(status);

CREATE INDEX idx_import_history_organization_id
    ON import_history(organization_id);

CREATE INDEX idx_import_history_system_user_id
    ON import_history(system_user_id);

CREATE INDEX idx_import_history_details_import_history_id
    ON import_history_details(import_history_id);