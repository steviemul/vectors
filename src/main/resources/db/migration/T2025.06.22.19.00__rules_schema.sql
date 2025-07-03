CREATE TABLE IF NOT EXISTS vector_store_rules (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    sarifMetadata json,
    embedding vector(1536)
);

CREATE INDEX ON vector_store_rules USING HNSW (embedding vector_cosine_ops);