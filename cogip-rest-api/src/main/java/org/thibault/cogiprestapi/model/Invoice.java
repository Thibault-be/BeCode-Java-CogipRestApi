package org.thibault.cogiprestapi.model;

import java.math.BigDecimal;

public class Invoice {
  
  private int id;
  private int companyId;
  private int contactId;
  private String invoiceNumber;
  private BigDecimal value;
  private String currency;
  private String type;
  private String status;
  
  public Invoice(){}
  
  public Invoice(int id, int companyId, int contactId, String invoiceNumber, BigDecimal value, String currency, String type, String status) {
    this.id = id;
    this.companyId = companyId;
    this.contactId = contactId;
    this.invoiceNumber = invoiceNumber;
    this.value = value;
    this.currency = currency;
    this.type = type;
    this.status = status;
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getCompanyId() {
    return companyId;
  }
  
  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }
  
  public int getContactId() {
    return contactId;
  }
  
  public void setContactId(int contactId) {
    this.contactId = contactId;
  }
  
  public String getInvoiceNumber() {
    return invoiceNumber;
  }
  
  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }
  
  public BigDecimal getValue() {
    return value;
  }
  
  public void setValue(BigDecimal value) {
    this.value = value;
  }
  
  public String getCurrency() {
    return currency;
  }
  
  public void setCurrency(String currency) {
    this.currency = currency;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
}
