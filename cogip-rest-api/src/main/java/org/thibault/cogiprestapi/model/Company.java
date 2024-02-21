package org.thibault.cogiprestapi.model;

public class Company {
  
  private int id;
  private String name;
  private String country;
  private String vat;
  private String type;
  
  public Company(String name, String country, String vat, String type){
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
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
}
