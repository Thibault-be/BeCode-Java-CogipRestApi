package org.thibault.cogiprestapi.exceptions;

public class IdNotFoundException extends RuntimeException{
  
  public IdNotFoundException(String message){
    super(message);
  }
}
