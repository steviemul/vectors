package io.steviemul.vectors.service;

import io.steviemul.vectors.entity.DocumentRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class VectorsService {

  public static final String EMBEDDING = "embedding";
  public static final String TYPE = "type";
  public static final String ID = "id";
  public static final String CATEGORY = "category";
  public static final String SEVERITY = "severity";

  private final EmbeddingModel embeddingModel;
  private final VectorStore vectorStore;
  private final TemplateService templateService;

  public VectorsService(
      EmbeddingModel embeddingModel,
      VectorStore vectorStore,
      TemplateService templateService) {
    this.embeddingModel = embeddingModel;
    this.vectorStore = vectorStore;
    this.templateService = templateService;
  }

  public void save(DocumentRequest documentRequest) {

    String embeddingContents = templateService.renderCodeEmbedding(documentRequest);

    Document document = new Document(
        embeddingContents,
        Map.of(
            TYPE, documentRequest.type(),
            CATEGORY, documentRequest.category(),
            SEVERITY, documentRequest.severity(),
            ID, documentRequest.id()));

    vectorStore.add(List.of(document));
  }

  public void delete(DocumentRequest documentRequest) {
    Filter.Expression expression = createFilterExpression(documentRequest);

    if (documentRequest.type() == null || documentRequest.id() == null) {
      throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Must specify type or id");
    }

    vectorStore.delete(expression);
  }

  public List<Document> search(DocumentRequest documentRequest) {

    String embeddingContents = templateService.renderCodeEmbedding(documentRequest);

    SearchRequest searchRequest = SearchRequest.builder()
        .query(embeddingContents)
        .similarityThreshold(0.1)
        .filterExpression(createFilterExpression(documentRequest))
        .topK(5)
        .build();

    return vectorStore.similaritySearch(searchRequest);
  }

  public Map<String, EmbeddingResponse> createEmbedding(String contents) {
    EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(contents));

    return Map.of(EMBEDDING, embeddingResponse);
  }

  private Filter.Expression createFilterExpression(DocumentRequest request) {

    FilterExpressionBuilder builder = new FilterExpressionBuilder();

    if (request.id() != null && request.type() != null) {
      return builder.and(builder.eq(ID, request.id()), builder.in(TYPE, request.type())).build();
    }

    if (request.id() != null) {
      return builder.eq(ID, request.id()).build();
    }

    if (request.type() != null) {
      return builder.in(TYPE, request.type()).build();
    }

    return null;
  }
}
