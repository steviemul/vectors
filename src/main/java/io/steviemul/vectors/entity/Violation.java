package io.steviemul.vectors.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

public class Violation {

  @Id private String id;

  @Column(name = "code_embedding")
  @JdbcTypeCode(SqlTypes.VECTOR)
  private List<Double> codeEmbedding;
}
