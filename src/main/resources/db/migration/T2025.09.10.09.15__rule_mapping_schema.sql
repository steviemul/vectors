CREATE TABLE IF NOT EXISTS rule_mappings (
    id BIGSERIAL PRIMARY KEY,
    rule_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS rule_mapping_score (
    id BIGSERIAL PRIMARY KEY,
    rule_mapping_id BIGINT NOT NULL,
    rule_id VARCHAR(255) NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_rule_mapping
        FOREIGN KEY (rule_mapping_id)
        REFERENCES rule_mappings(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_rule_mapping_score_mapping_id ON rule_mapping_score(rule_mapping_id);
CREATE INDEX IF NOT EXISTS idx_rule_mapping_score_rule_id ON rule_mapping_score(rule_id);
