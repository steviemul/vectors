package io.steviemul.vectors.configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfiguration {

  private final OllamaApi ollamaApi = OllamaApi.builder().build();

  private final ModelManagementOptions modelManagementOptions = ModelManagementOptions.builder()
      .pullModelStrategy(PullModelStrategy.WHEN_MISSING)
      .build();

  @Bean("ollamaTextEmbeddingModel")
  public EmbeddingModel ollamaTextEmbeddingModel() {

    OllamaOptions options = OllamaOptions.builder()
        .model("nomic-embed-text")
        .build();

    return OllamaEmbeddingModel.builder()
        .ollamaApi(ollamaApi)
        .defaultOptions(options)
        .modelManagementOptions(modelManagementOptions)
        .build();
  }

  @Bean("ollamaBertEmbeddingModel")
  public EmbeddingModel ollamaBertEmbeddingModel() {

    OllamaOptions options = OllamaOptions.builder()
        .model("nomic-embed-text")
        .build();

    return OllamaEmbeddingModel.builder()
        .ollamaApi(ollamaApi)
        .defaultOptions(options)
        .modelManagementOptions(modelManagementOptions)
        .build();
  }
}
