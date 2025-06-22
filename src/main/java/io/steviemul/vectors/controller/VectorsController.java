package io.steviemul.vectors.controller;

import io.steviemul.vectors.entity.DocumentRequest;
import io.steviemul.vectors.service.CodeVectorsService;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class VectorsController {

  public static final String DOCUMENTS_URI = "/documents";
  public static final String EMBEDDING_URI = "/embedding";

  private final CodeVectorsService vectorsService;

  public VectorsController(CodeVectorsService vectorsService) {
    this.vectorsService = vectorsService;
  }

  @GetMapping(EMBEDDING_URI)
  public Map<String, EmbeddingResponse> getEmbedding(
      @RequestParam(defaultValue = "Something to embed") String contents) {
    return vectorsService.createEmbedding(contents);
  }

  @PostMapping(value = DOCUMENTS_URI, consumes = "application/json", produces = "application/json")
  public ResponseEntity<Void> saveDocument(@RequestBody DocumentRequest documentRequest) {

    vectorsService.save(documentRequest);

    return ResponseEntity.accepted().build();
  }

  @DeleteMapping(value = DOCUMENTS_URI)
  public ResponseEntity<Void> deleteDocument(@RequestBody DocumentRequest documentRequest) {

    vectorsService.delete(documentRequest);

    return ResponseEntity.noContent().build();
  }

  @GetMapping(DOCUMENTS_URI)
  public List<Document> getDocuments(DocumentRequest documentRequest) {
    return vectorsService.search(documentRequest);
  }
}
