package io.steviemul.vectors.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.steviemul.vectors.entity.DocumentResponse;
import io.steviemul.vectors.entity.OllamaRule;
import io.steviemul.vectors.entity.RuleMapping;
import io.steviemul.vectors.entity.RuleMappingScore;
import io.steviemul.vectors.repository.OllamaRuleRepository;
import io.steviemul.vectors.repository.RuleMappingRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleMappingService {

  private static final String ID = "id";

  private final ObjectMapper objectMapper;
  private final OllamaRuleRepository ollamaRuleRepository;
  private final RuleMappingRepository ruleMappingRepository;

  public List<DocumentResponse> getRuleMappings(String ruleId) {

    return ruleMappingRepository.findByRuleId(ruleId)
        .map(this::ruleMappingToOllamaRules)
        .orElse(Collections.emptyList());
  }

  public void saveRuleMappings(String ruleId, List<DocumentResponse> documentResponses) {

    RuleMapping ruleMapping = new RuleMapping();

    ruleMapping.setRuleId(ruleId);

    List<RuleMappingScore> ruleMappingScores = documentResponsesToRuleMappingScores(documentResponses);

    ruleMappingScores.forEach(s -> s.setRuleMapping(ruleMapping));
    ruleMapping.setMappings(ruleMappingScores);

    ruleMappingRepository.save(ruleMapping);
  }

  private List<RuleMappingScore> documentResponsesToRuleMappingScores(List<DocumentResponse> documentResponses) {

    return documentResponses.stream()
        .map(this::documentResponseToRuleMappingScore)
        .toList();
  }

  private RuleMappingScore documentResponseToRuleMappingScore(DocumentResponse documentResponse) {

    RuleMappingScore ruleMappingScore = new RuleMappingScore();

    ruleMappingScore.setScore(documentResponse.score());
    ruleMappingScore.setRuleId(String.valueOf(documentResponse.metadata().get(ID)));

    return ruleMappingScore;
  }

  private List<DocumentResponse> ruleMappingToOllamaRules(RuleMapping ruleMapping) {

    return ruleMapping.getMappings()
  .stream()
  .map(this::ruleMappingToDocument)
  .filter(Optional::isPresent)
  .map(Optional::get)
        .toList();
  }

  private Optional<DocumentResponse> ruleMappingToDocument(RuleMappingScore ruleMappingScore) {

    try {
      OllamaRule ollamaRule = ollamaRuleRepository.findByRuleId(ruleMappingScore.getRuleId());

      Map<String, Object> metadata = objectMapper.readValue(ollamaRule.getMetadata(),
          new TypeReference<>() {});

      return Optional.of(new DocumentResponse(ruleMappingScore.getScore(), metadata));
    }
    catch (Exception e) {
      log.error("Unable to read rule metadata", e);
      return Optional.empty();
    }
  }
}
