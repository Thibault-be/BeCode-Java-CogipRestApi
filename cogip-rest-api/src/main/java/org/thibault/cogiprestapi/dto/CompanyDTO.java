package org.thibault.cogiprestapi.dto;

import org.springframework.security.core.parameters.P;

import java.util.List;

public class CompanyDTO {
  
  private String name;
  private String country;
  private String vat;
  private List<Integer> invoices;
  private String contact;
  
  public CompanyDTO(){}
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getCountry() {
    return country;
  }
  
  public void setCountry(String country) {
    this.country = country;
  }
  
  public String getVat() {
    return vat;
  }
  
  public void setVat(String vat) {
    this.vat = vat;
  }
  
  public List<Integer> getInvoices() {
    return invoices;
  }
  
  public void setInvoices(List<Integer> invoices) {
    this.invoices = invoices;
  }
  
  public String getContact() {
    return contact;
  }
  
  public void setContact(String contact) {
    this.contact = contact;
  }
}
