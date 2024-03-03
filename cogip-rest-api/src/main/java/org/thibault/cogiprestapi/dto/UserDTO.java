package org.thibault.cogiprestapi.dto;

import org.thibault.cogiprestapi.enums.UserRole;

public class UserDTO {
  
  private int id;
  private String username;
  private UserRole role;
  
  public UserDTO(){}
  
  public UserDTO(int id, String username, UserRole role) {
    this.id = id;
    this.username = username;
    this.role = role;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public UserRole getRole() {
    return role;
  }
  
  public void setRole(UserRole role) {
    this.role = role;
  }
  
  public int getId(){
    return this.id;
  }
  
  public void setId(int id){
    this.id = id;
  }
}
