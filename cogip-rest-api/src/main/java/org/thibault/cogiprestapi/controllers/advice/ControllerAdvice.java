package org.thibault.cogiprestapi.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thibault.cogiprestapi.exceptions.*;


@RestControllerAdvice
public class ControllerAdvice {
  
  @ExceptionHandler (IdNotFoundException.class)
  public ResponseEntity<String> handlerIdNotFoundException(IdNotFoundException idNotFoundException){
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(idNotFoundException.getMessage());
  }
  
  @ExceptionHandler (ResultSetEmptyException.class)
  public ResponseEntity<String> handlerResultSetEmptyException(ResultSetEmptyException resultSetEmptyException){
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(resultSetEmptyException.getMessage());
  }
  
  @ExceptionHandler (ParametersMissingException.class)
  public ResponseEntity<String> handlerParametersMissingException(ParametersMissingException parametersMissingException){
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(parametersMissingException.getMessage());
  }
  
  @ExceptionHandler (DuplicateValueException.class)
  public ResponseEntity<String> handleDuplicateValueException(DuplicateValueException duplicateValueException){
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(duplicateValueException.getMessage());
  }
  
  @ExceptionHandler (IllegalParametersException.class)
  public ResponseEntity<String> handleIllegalParametersException(IllegalParametersException illegalParametersException){
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(illegalParametersException.getMessage());
  }
  
  @ExceptionHandler (HttpMessageNotReadableException.class)
  public String handleHttpMessageNotReadableException (HttpMessageNotReadableException httpMessageNotReadableException){
    return "http not readable";
  }
}
