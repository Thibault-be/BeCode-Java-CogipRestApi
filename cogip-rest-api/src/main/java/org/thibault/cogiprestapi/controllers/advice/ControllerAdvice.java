package org.thibault.cogiprestapi.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thibault.cogiprestapi.exceptions.DuplicateValueException;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.ParametersMissingException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;


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
            .status(HttpStatus.NO_CONTENT)
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
  
}
