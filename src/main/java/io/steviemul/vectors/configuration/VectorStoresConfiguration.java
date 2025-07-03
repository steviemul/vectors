package io.steviemul.vectors.configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class VectorStoresConfiguration {

  @Bean("openAiRulesVectorStore")
  public VectorStore openAiRulesVectorStore(
      JdbcTemplate jdbcTemplate,
      @Qualifier("openAiTextEmbeddingModel") EmbeddingModel embeddingModel) {

    return PgVectorStore.builder(jdbcTemplate, embeddingModel)
        .vectorTableName("vector_store_openai_rules")
        .build();
  }

  @Bean("ollamaRulesVectorStore")
  public VectorStore ollamaRulesVectorStore(
      JdbcTemplate jdbcTemplate,
      @Qualifier("ollamaTextEmbeddingModel") EmbeddingModel embeddingModel) {

    return PgVectorStore.builder(jdbcTemplate, embeddingModel)
        .vectorTableName("vector_store_ollama_rules")
        .build();
  }

}
