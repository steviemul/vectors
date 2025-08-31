package io.steviemul.vectors.service;

import io.steviemul.vectors.repository.OllamaRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OllamaRulesService {

  private final OllamaRuleRepository ollamaRuleRepository;

  public int getRulesCountByVendor(String vendor) {
    return ollamaRuleRepository.countAllByVendor(vendor);
  }
}
