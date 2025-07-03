package io.steviemul.vectors.configuration;

import io.steviemul.vectors.service.RulesVectorService;
import io.steviemul.vectors.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VectorServicesConfiguration {

  private final TemplateService templateService;

  @Bean("openAiRulesVectorService")
  public RulesVectorService openAiRulesVectorService(@Qualifier("openAiRulesVectorStore") VectorStore vectorStore) {
    return new RulesVectorService(vectorStore, templateService);
  }

  @Bean("ollamaRulesVectorService")
  public RulesVectorService ollamaRulesVectorService(@Qualifier("ollamaRulesVectorStore") VectorStore vectorStore) {
    return new RulesVectorService(vectorStore, templateService);
  }
}
