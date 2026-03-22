ALTER TABLE users
    ADD is_enabled BOOLEAN;

UPDATE users
SET is_enabled = 'true'
WHERE is_enabled IS NULL;
ALTER TABLE users
    ALTER COLUMN is_enabled SET NOT NULL;
