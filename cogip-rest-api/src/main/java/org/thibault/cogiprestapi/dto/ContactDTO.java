package org.thibault.cogiprestapi.dto;

import java.util.List;

public class ContactDTO {
  
  private String firstname;
  private String lastname;
  private String email;
  private String phone;
  private String companyName;
  private List<Integer> invoiceNumbers;
  
  public ContactDTO(){}
  
  public ContactDTO(String firstname, String lastname, String email, String phone, String companyName, List<Integer> invoiceNumber) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
    this.companyName = companyName;
    this.invoiceNumbers = invoiceNumber;
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
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public String getCompanyName() {
    return companyName;
  }
  
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }
  
  public List<Integer> getInvoiceNumbers() {
    return invoiceNumbers;
  }
  
  public void setInvoiceNumbers(List<Integer> invoiceNumbers) {
    this.invoiceNumbers = invoiceNumbers;
  }
}


