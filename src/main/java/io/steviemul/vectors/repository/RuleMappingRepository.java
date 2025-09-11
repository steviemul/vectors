package io.steviemul.vectors.repository;

import io.steviemul.vectors.entity.RuleMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RuleMappingRepository extends JpaRepository<RuleMapping, Long> {

  Optional<RuleMapping> findByRuleId(@Param("ruleId") String ruleId);
}
