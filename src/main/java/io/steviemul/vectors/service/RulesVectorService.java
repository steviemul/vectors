package io.steviemul.vectors.service;

import static org.springframework.ai.model.ModelOptionsUtils.objectToMap;

import io.steviemul.vectors.entity.DocumentResponse;
import io.steviemul.vectors.entity.QueryAdjustments;
import io.steviemul.vectors.entity.RuleRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder.Op;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RulesVectorService {

  private final VectorStore rulesVectorStore;
  private final TemplateService templateService;

  private static final String VENDOR = "vendor";

  public RulesVectorService(
      @Qualifier("rulesVectorStore") VectorStore vectorStore,
      TemplateService templateService) {
    this.rulesVectorStore = vectorStore;
    this.templateService = templateService;
  }

  public void save(RuleRequest ruleRequest) {

    String embeddingContents = templateService.renderRuleEmbedding(ruleRequest);

    Document document = new Document(
        embeddingContents,
        templateService.objectToMap(ruleRequest));

    rulesVectorStore.add(List.of(document));
  }

  public List<DocumentResponse> search(String vendor, QueryAdjustments adjustments, RuleRequest ruleRequest) {

    String embeddingContents = templateService.renderRuleEmbedding(ruleRequest);

    SearchRequest.Builder searchRequestBuilder = SearchRequest.builder()
        .query(embeddingContents)
        .similarityThreshold(adjustments.threshold())
        .topK(adjustments.size());

    createFilterExpression(vendor, adjustments.exact(), ruleRequest)
        .ifPresent(e -> searchRequestBuilder.filterExpression(e));

    SearchRequest searchRequest = searchRequestBuilder.build();

    return rulesVectorStore.similaritySearch(searchRequest)
        .stream().map(this::documentToResponse)
        .toList();
  }

  private DocumentResponse documentToResponse(Document document) {
    return new DocumentResponse(document.getScore(), document.getMetadata());
  }

  private Optional<Filter.Expression> createFilterExpression(String vendor, boolean exact, RuleRequest ruleRequest) {

    FilterExpressionBuilder builder = new FilterExpressionBuilder();

    Op op = null;

    if (StringUtils.hasText(vendor)) {
      op = builder.eq(VENDOR, vendor);
    }

    if (exact) {
      Op metatadaOp = createMetadataFilterOp(ruleRequest);

      if (metatadaOp != null) {
        op = (op == null) ? metatadaOp : builder.and(op, metatadaOp);
      }
    }

    return Optional.ofNullable(op).map(Op::build);
  }

  private Op createMetadataFilterOp(RuleRequest request) {

    FilterExpressionBuilder builder = new FilterExpressionBuilder();

    Op op = null;
    Map<String, Object> data = objectToMap(request);

    for (Map.Entry<String, Object> entry : data.entrySet()) {
      if (entry.getValue() != null) {
        Op eq = builder.eq(entry.getKey(), entry.getValue());

        op = (op == null) ? eq : builder.and(op, eq);
      }
    }

    return op;
  }
}
