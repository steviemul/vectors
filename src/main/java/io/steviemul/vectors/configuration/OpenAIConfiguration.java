package io.steviemul.vectors.configuration;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration {

  private final OpenAiApi openAiApi;

  public OpenAIConfiguration(@Value("${spring.ai.openai.api-key}") String openApiKey) {
    openAiApi = OpenAiApi.builder()
        .apiKey(openApiKey)
        .build();
  }

  @Bean("openAiTextEmbeddingModel")
  public EmbeddingModel openAiTextEmbeddingModel() {

    OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions
        .builder()
        .model("text-embedding-3-small")
        .build();

    return new OpenAiEmbeddingModel(openAiApi, MetadataMode.EMBED, options);
  }
}
