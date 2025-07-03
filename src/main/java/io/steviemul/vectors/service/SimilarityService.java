package io.steviemul.vectors.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SimilarityService {

  private final EmbeddingModel codeEmbeddingModel;

  public SimilarityService(@Qualifier("ollamaBertEmbeddingModel") EmbeddingModel codeEmbeddingModel) {
    this.codeEmbeddingModel = codeEmbeddingModel;
  }

  public double getSimilarity(String first, String second) {

    float[] embeddingA = codeEmbeddingModel.embed(first);
    float[] embeddingB = codeEmbeddingModel.embed(second);

    return getCosineSimilarity(embeddingA, embeddingB);
  }

  public double getCosineSimilarity(float[] embeddingA, float[] embeddingB) {

    if (embeddingA.length != embeddingB.length) {
      throw new IllegalArgumentException("Embedding lengths do not match");
    }

    double dotProduct = 0, normA = 0, normB = 0;

    for (int i = 0; i < embeddingA.length; i++) {
      dotProduct += embeddingA[i] * embeddingB[i];

      normA += Math.pow(embeddingA[i], 2);
      normB += Math.pow(embeddingB[i], 2);
    }

    if (normA == 0 && normB == 0) {
      return 0.0;
    }

    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }
}
