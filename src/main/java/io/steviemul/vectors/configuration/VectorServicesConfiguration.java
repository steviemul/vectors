package io.steviemul.vectors.configuration;

import io.steviemul.vectors.service.RuleMappingService;
import io.steviemul.vectors.service.RulesVectorService;
import io.steviemul.vectors.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VectorServicesConfiguration {

  private final TemplateService templateService;
  private final EmbeddingModel ollamaTextEmbeddingModel;
  private final EmbeddingModel openAiTextEmbeddingModel;
  private final RuleMappingService ruleMappingService;

  @Bean("openAiRulesVectorService")
  public RulesVectorService openAiRulesVectorService(@Qualifier("openAiRulesVectorStore") VectorStore vectorStore) {
    return new RulesVectorService(vectorStore, templateService, openAiTextEmbeddingModel, ruleMappingService);
  }

  @Bean("ollamaRulesVectorService")
  public RulesVectorService ollamaRulesVectorService(@Qualifier("ollamaRulesVectorStore") VectorStore vectorStore) {
    return new RulesVectorService(vectorStore, templateService, ollamaTextEmbeddingModel, ruleMappingService);
  }
}
