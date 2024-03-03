package org.thibault.cogiprestapi.exceptions;

public class ResultSetEmptyException extends RuntimeException{
  
  public ResultSetEmptyException (String message){
    super(message);
  }
}
