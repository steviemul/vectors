package io.steviemul.vectors.utils;

import com.contrastsecurity.sarif.Result;
import com.contrastsecurity.sarif.Run;
import com.contrastsecurity.sarif.SarifSchema210;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.steviemul.vectors.entity.DocumentRequest;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

public class ResultsLoader {

  private static final String DOCUMENTS_URL = "http://localhost:8080/documents";
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String RESULTS_PATH = "/results/results.sarif";
  private static final HttpClient httpClient = HttpClient.newHttpClient();

  public static void main(String[] args) throws Exception {

    SarifSchema210 sarifSchema210 = readResults();

    List<Result> results = getResults(sarifSchema210);

    for (Result result : results) {
      postResult(result);
    }
  }

  private static void postResult(Result result) throws Exception {

    DocumentRequest documentRequest = resultToDocumentRequest(result);

    String requestBody = objectMapper.writeValueAsString(documentRequest);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(DOCUMENTS_URL))
        .headers("Content-Type", "application/json;charset=UTF-8")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 202) {
      System.out.println("Result saved successfully");
    }
    else {
      System.err.println("Error saving result : " + response.statusCode());
    }
  }

  private static DocumentRequest resultToDocumentRequest(Result result) {

    String type = result.getRuleId();
    String description = result.getMessage().getText();
    String filename = result.getLocations().get(0).getPhysicalLocation().getArtifactLocation().getUri();
    Integer lineNumber = result.getLocations().get(0).getPhysicalLocation().getRegion().getStartLine();
    String code = result.getLocations().get(0).getPhysicalLocation().getRegion().getSnippet().getText();
    String language = result.getLocations().get(0).getPhysicalLocation().getRegion().getSourceLanguage();
    String severity = (String) result.getProperties().getAdditionalProperties().get("severity");

    return new DocumentRequest(
        filename,
        type,
        lineNumber,
        description,
        code,
        "unknown",
        "unknown",
        severity,
        language,
        UUID.randomUUID());
  }

  private static List<Result> getResults(SarifSchema210 sarifSchema210) {

    Run run = sarifSchema210.getRuns().get(0);

    return run.getResults();
  }

  private static SarifSchema210 readResults() throws Exception {

    try (InputStream in = ResultsLoader.class.getResourceAsStream(RESULTS_PATH)){
      SarifSchema210 sarifSchema210 = objectMapper.readValue(in, SarifSchema210.class);

      return sarifSchema210;
    }
  }
}
