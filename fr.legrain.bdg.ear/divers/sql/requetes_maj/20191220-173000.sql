--set schema 'demo';
DROP INDEX idx_ta_espace_client_email;

CREATE UNIQUE INDEX ta_espace_client_email_idx_a ON ta_espace_client (email, id_tiers)
WHERE id_tiers IS NOT NULL;

CREATE UNIQUE INDEX ta_espace_client_email_idx_b ON ta_espace_client (email)
WHERE id_tiers IS NULL;

