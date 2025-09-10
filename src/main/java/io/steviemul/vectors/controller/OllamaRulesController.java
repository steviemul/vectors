package io.steviemul.vectors.controller;

import io.steviemul.vectors.entity.DocumentResponse;
import io.steviemul.vectors.entity.QueryAdjustments;
import io.steviemul.vectors.entity.RuleRequest;
import io.steviemul.vectors.service.RulesVectorService;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaRulesController {

  public static final String RULES_URI = "/ollama/rules";
  public static final String RULE_IDS_URL = RULES_URI + "/ids";
  public static final String ID = "id";

  private final RulesVectorService rulesVectorService;

  public OllamaRulesController(
      @Qualifier("ollamaRulesVectorService") RulesVectorService rulesVectorService) {
    this.rulesVectorService = rulesVectorService;
  }

  @PostMapping(value = RULES_URI, consumes = "application/json", produces = "application/json")
  public ResponseEntity<Void> saveDocument(@RequestBody RuleRequest ruleRequest) {

    rulesVectorService.save(ruleRequest);

    return ResponseEntity.accepted().build();
  }

  @GetMapping( "/ollama/embedding")
  public float[] getEmbeddings(RuleRequest ruleRequest) {
    return rulesVectorService.getEmbedding(ruleRequest);
  }

  @GetMapping(RULES_URI)
  public List<DocumentResponse> getRules(QueryAdjustments adjustments, RuleRequest ruleRequest) {
    return rulesVectorService.search(null, adjustments, ruleRequest);
  }

  @GetMapping(RULES_URI + "/{vendor}")
  public List<DocumentResponse> getRulesForVendor(
      @PathVariable("vendor") final String vendor,
      QueryAdjustments adjustments, RuleRequest ruleRequest) {
    return rulesVectorService.search(vendor, adjustments, ruleRequest);
  }

  @GetMapping(RULE_IDS_URL + "/{vendor}")
  public Map<Object, Double> getSimilarRulesForVendor(
      @PathVariable("vendor") final String vendor,
      QueryAdjustments adjustments, RuleRequest ruleRequest) {

    return rulesVectorService.search(vendor, adjustments, ruleRequest)
        .stream().collect(
            Collectors.toMap(
                d -> d.metadata().get(ID),
                DocumentResponse::score,
                (d1, d2) -> d1,
                LinkedHashMap::new));
  }

}
