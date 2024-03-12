package org.thibault.cogiprestapi.security;


public class AuthResponseDTO {
  
  private String accessToken;
  private String tokenType = "Bearer ";
  
  public AuthResponseDTO(String accessToken) {
    this.accessToken = accessToken;
  }
}
