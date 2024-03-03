package org.thibault.cogiprestapi.model;

import org.thibault.cogiprestapi.enums.UserRole;

public class User {
  
  private int id;
  private String username;
  private String password;
  private UserRole role;
  
  public User(){};
  
  public User(String username, String password, UserRole role){
    this.username = username;
    this.password = password;
    this.role = role;
  }
  
  public int getId(){
    return this.id;
  }
  
  public void setId(int id){
    this.id = id;
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
