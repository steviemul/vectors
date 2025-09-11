package io.steviemul.vectors.repository;

import io.steviemul.vectors.entity.OllamaRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OllamaRuleRepository extends JpaRepository<OllamaRule, UUID> {

	@Query(value = "SELECT * FROM vector_store_ollama_rules WHERE metadata->>'vendor' = :vendor", nativeQuery = true)
	List<OllamaRule> findAllByVendor(@Param("vendor") String vendor);

  @Query(value = "SELECT COUNT(*) FROM vector_store_ollama_rules WHERE metadata->>'vendor' = :vendor", nativeQuery = true)
	int countAllByVendor(@Param("vendor") String vendor);

  @Query(value = "SELECT * FROM vector_store_ollama_rules WHERE metadata->>'id' = ANY(:ruleIds)", nativeQuery = true)
  List<OllamaRule> findAllByRuleIdIn(@Param("ruleIds") List<String> ruleIds);

  @Query(value = "SELECT * FROM vector_store_ollama_rules WHERE metadata->>'id' = :ruleId", nativeQuery = true)
  OllamaRule findByRuleId(@Param("ruleId") String ruleId);
}
