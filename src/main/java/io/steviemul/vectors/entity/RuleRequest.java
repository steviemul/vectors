package io.steviemul.vectors.entity;

import java.util.List;

public record RuleRequest(
    String id,
    String name,
    String severity,
    String category,
    List<String> cwe,
    String description,
    String risk,
    String issue,
    String advice,
    List<String> language,
    String vendor) {

}
