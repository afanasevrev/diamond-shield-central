ALTER TABLE readers
ADD COLUMN IF NOT EXISTS perco_exdev_number INTEGER;

ALTER TABLE readers
ADD COLUMN IF NOT EXISTS perco_direction INTEGER;

CREATE INDEX IF NOT EXISTS idx_readers_perco_mapping
ON readers(controller_id, perco_exdev_number, perco_direction);