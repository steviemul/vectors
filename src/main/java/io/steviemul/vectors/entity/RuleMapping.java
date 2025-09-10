package io.steviemul.vectors.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.ArrayList;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rule_mappings")
public class RuleMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String ruleId;

  @OneToMany(mappedBy = "ruleMapping", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RuleMappingScore> mappings = new ArrayList<>();
}
