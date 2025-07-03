package io.steviemul.vectors.controller;

import io.steviemul.vectors.entity.DocumentResponse;
import io.steviemul.vectors.entity.QueryAdjustments;
import io.steviemul.vectors.entity.RuleRequest;
import io.steviemul.vectors.service.RulesVectorService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RulesController {

  public static final String RULES_URI = "/rules";

  private final RulesVectorService rulesVectorService;

  @PostMapping(value = RULES_URI, consumes = "application/json", produces = "application/json")
  public ResponseEntity<Void> saveDocument(@RequestBody RuleRequest ruleRequest) {

    rulesVectorService.save(ruleRequest);

    return ResponseEntity.accepted().build();
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
}
