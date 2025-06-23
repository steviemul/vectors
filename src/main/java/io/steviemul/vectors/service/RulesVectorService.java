package io.steviemul.vectors.service;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RulesVectorService {

  private final VectorStore rulesVectorStore;

  public RulesVectorService(@Qualifier("rulesVectorStore") VectorStore vectorStore) {
    this.rulesVectorStore = vectorStore;
  }
}
