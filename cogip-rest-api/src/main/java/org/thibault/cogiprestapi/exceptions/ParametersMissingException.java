package org.thibault.cogiprestapi.exceptions;

public class ParametersMissingException extends RuntimeException{
  
  public ParametersMissingException(String message){
    super(message);
  }
}
