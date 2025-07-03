CREATE TABLE IF NOT EXISTS vector_store_openai_rules (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(1536)
);

CREATE INDEX ON vector_store_openai_rules USING HNSW (embedding vector_cosine_ops);