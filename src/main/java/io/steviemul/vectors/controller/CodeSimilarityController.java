package io.steviemul.vectors.controller;

import io.steviemul.vectors.service.SimilarityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CodeSimilarityController {

  private final static String COMPARE_URI = "/ollama/compare";
  private final SimilarityService similarityService;

  @GetMapping(COMPARE_URI)
  public ResponseEntity<ComparisonResponse> getSimilarity(String first, String second) {
    double similarity = similarityService.getSimilarity(first, second);

    return ResponseEntity.ok(new ComparisonResponse(first, second, similarity));
  }

  private record ComparisonResponse(String first, String second, double similarity) {}
}
