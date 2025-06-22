package io.steviemul.vectors.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.steviemul.vectors.entity.DocumentRequest;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TemplateService {

  private static final String UNKNOWN = "UNKNOWN";

  private static final String CODE_TEMPLATE_RESOURCE = "templates/CodeEmbedding.template";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public String renderCodeEmbedding(DocumentRequest documentRequest) {

    PromptTemplate template = PromptTemplate.builder()
        .resource(new ClassPathResource(CODE_TEMPLATE_RESOURCE))
        .build();

    Map<String, Object> data = objectMapper
        .convertValue(documentRequest, new TypeReference<>() {});

    for (Map.Entry<String, Object> entry : data.entrySet()) {
      if (entry.getValue() == null) {
        entry.setValue(UNKNOWN);
      }
    }

    return template.render(data);
  }
}
