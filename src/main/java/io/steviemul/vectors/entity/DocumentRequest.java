package io.steviemul.vectors.entity;

import java.util.UUID;

public record DocumentRequest(
    String filename,
    String type,
    Integer lineNumber,
    String description,
    String code,
    String reason,
    String category,
    String severity,
    UUID id) {

}
