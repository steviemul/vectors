package io.steviemul.vectors.entity;

public record QueryAdjustments(Boolean exact, Double threshold, Integer size) {

  public QueryAdjustments {
    if (exact == null) exact = false;
    if (threshold == null) threshold = 0.5;
    if (size == null) size = 5;
  }
}
