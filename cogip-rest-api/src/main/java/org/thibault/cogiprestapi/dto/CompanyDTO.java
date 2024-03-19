package org.thibault.cogiprestapi.dto;

import java.util.List;

public class CompanyDTO {
  
  private String name;
  private String country;
  private String vat;
  private List<Integer> invoices;
  private List<String> contacts;
  
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
  
  public List<String> getContacts() {
    return contacts;
  }
  
  public void setContacts(List<String> contacts) {
    this.contacts = contacts;
  }
}
