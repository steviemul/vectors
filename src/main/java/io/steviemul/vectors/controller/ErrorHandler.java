package io.steviemul.vectors.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Error> handleException(ResponseStatusException e) {

    return ResponseEntity
        .status(e.getStatusCode().value())
        .body(new Error(e.getReason()));
  }

  private record Error(String message) {}
}
