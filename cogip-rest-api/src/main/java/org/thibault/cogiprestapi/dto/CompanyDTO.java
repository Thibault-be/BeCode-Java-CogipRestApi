package org.thibault.cogiprestapi.dto;

import org.thibault.cogiprestapi.enums.CompanyType;

import java.util.List;

public class CompanyDTO {
  
  private Integer id;
  private String name;
  private String country;
  private String vat;
  private CompanyType type;
  private List<Integer> invoices;
  private List<String> contacts;
  
  public CompanyDTO(){}
  
  public CompanyDTO(String name, String country, String vat, CompanyType type, List<Integer> invoices, List<String> contacts) {
    this.name = name;
    this.country = country;
    this.vat = vat;
    this.type = type;
    this.invoices = invoices;
    this.contacts = contacts;
  }
  
  public Integer getId(){
    return this.id;
  }
  
  public void setId(Integer id){
    this.id = id;
  }
  
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
  
  public CompanyType getType() {
    return type;
  }
  
  public void setType(CompanyType type) {
    this.type = type;
  }
}
