package org.thibault.cogiprestapi.model;

public class Contact {
  
  private int id;
  private String firstname;
  private String lastname;
  private String phone;
  private String email;
  private int companyId;
  
  public Contact() { }
  
  public Contact(int id, String firstname, String lastname, String phone, String email, int companyId) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.phone = phone;
    this.email = email;
    this.companyId = companyId;
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getFirstname() {
    return firstname;
  }
  
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  
  public String getLastname() {
    return lastname;
  }
  
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public int getCompanyId() {
    return companyId;
  }
  
  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }
}

