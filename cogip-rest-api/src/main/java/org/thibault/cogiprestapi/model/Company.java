package org.thibault.cogiprestapi.model;

import org.thibault.cogiprestapi.enums.CompanyType;

public class Company {
  
  private int id;
  private String name;
  private String country;
  private String vat;
  private CompanyType type;
  
  public Company(){}
  
  public Company(String name, String country, String vat, CompanyType type){
    this.name = name;
    this.country = country;
    this.vat = vat;
    this.type = type;
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id){
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
  
  public CompanyType getType() {
    return type;
  }
  
  public void setType(CompanyType type) {
    this.type = type;
  }
}
