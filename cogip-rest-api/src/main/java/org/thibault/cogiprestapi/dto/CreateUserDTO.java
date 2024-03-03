package org.thibault.cogiprestapi.dto;

import org.thibault.cogiprestapi.enums.UserRole;

public class CreateUserDTO {

  private String username;
  private String password;
  private UserRole role;
  
  public CreateUserDTO(){}
  
  public CreateUserDTO(String username, String password, UserRole role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public UserRole getRole() {
    return role;
  }
  
  public void setRole(UserRole role) {
    this.role = role;
  }
}
