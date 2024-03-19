package org.thibault.cogiprestapi.dto;

import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class InvoiceDTO {
  
  private int invoiceNumber;
  private BigDecimal value;
  private Currency currency;
  private InvoiceStatus status;
  private String companyName;
  private CompanyType companyType;
  private String contact;
  private Timestamp timestamp;
  
  public InvoiceDTO(){}
  
  public int getInvoiceNumber() {
    return invoiceNumber;
  }
  
  public void setInvoiceNumber(int invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }
  
  public BigDecimal getValue() {
    return value;
  }
  
  public void setValue(BigDecimal value) {
    this.value = value;
  }
  
  public Currency getCurrency() {
    return currency;
  }
  
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }
  
  public InvoiceStatus getStatus() {
    return status;
  }
  
  public void setStatus(InvoiceStatus status) {
    this.status = status;
  }
  
  public String getCompanyName() {
    return companyName;
  }
  
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }
  
  public CompanyType getCompanyType() {
    return companyType;
  }
  
  public void setCompanyType(CompanyType companyType) {
    this.companyType = companyType;
  }
  
  public String getContact() {
    return contact;
  }
  
  public void setContact(String contact) {
    this.contact = contact;
  }
  
  public Timestamp getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}




