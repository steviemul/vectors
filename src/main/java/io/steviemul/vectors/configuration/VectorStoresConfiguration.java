package io.steviemul.vectors.configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class VectorStoresConfiguration {

  @Bean("codeVectorStore")
  public VectorStore codeVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
    return PgVectorStore.builder(jdbcTemplate, embeddingModel)
        .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
        .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
        .indexType(HNSW)                     // Optional: defaults to HNSW
        .schemaName("public")                // Optional: defaults to "public"
        .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
        .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
        .build();
  }

  @Bean("rulesVectorStore")
  public VectorStore rulesVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
    return PgVectorStore.builder(jdbcTemplate, embeddingModel)
        .vectorTableName("vector_store_rules")
        .build();
  }
}
