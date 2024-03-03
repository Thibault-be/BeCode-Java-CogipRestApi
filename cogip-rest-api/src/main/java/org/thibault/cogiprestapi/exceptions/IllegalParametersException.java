package org.thibault.cogiprestapi.exceptions;

public class IllegalParametersException extends RuntimeException{
  
  public IllegalParametersException(String message){
    super(message);
  }
}
