package io.steviemul.vectors.entity;

import java.util.Map;

public record DocumentResponse(double score, Map<String, Object> metadata) {}
