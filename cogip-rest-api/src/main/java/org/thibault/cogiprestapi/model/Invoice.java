package org.thibault.cogiprestapi.model;

import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;
import org.thibault.cogiprestapi.enums.InvoiceType;

import java.math.BigDecimal;

public class Invoice {
  
  private int id;
  private int companyId;
  private int contactId;
  private String invoiceNumber;
  private BigDecimal value;
  private Currency currency;
  private InvoiceType type;
  private InvoiceStatus status;
  
  public Invoice(){}
  
  public Invoice(int id, Integer companyId, Integer contactId, String invoiceNumber, BigDecimal value, Currency currency, InvoiceType type, InvoiceStatus status) {
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
  
  public Integer getCompanyId() {
    return companyId;
  }
  
  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }
  
  public Integer getContactId() {
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
  
  public Currency getCurrency() {
    return currency;
  }
  
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }
  
  public InvoiceType getType() {
    return type;
  }
  
  public void setType(InvoiceType type) {
    this.type = type;
  }
  
  public InvoiceStatus getStatus() {
    return status;
  }
  
  public void setStatus(InvoiceStatus status) {
    this.status = status;
  }
}
