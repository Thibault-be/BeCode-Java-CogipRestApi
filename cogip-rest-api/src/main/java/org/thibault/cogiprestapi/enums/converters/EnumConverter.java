package org.thibault.cogiprestapi.enums.converters;

import org.thibault.cogiprestapi.enums.*;

public class EnumConverter {
  
  public UserRole convertStringToRole(String role){
    if (role == null) return null;
    if (role.equalsIgnoreCase("admin")){
      return UserRole.ADMIN;
    }
    if (role.equalsIgnoreCase("accountant")){
      return UserRole.ACCOUNTANT;
    }
    if (role.equalsIgnoreCase("intern")){
      return UserRole.INTERN;
    }
    return null;
  }
  
  public CompanyType convertStringToCompanyType(String type){
    if (type == null) return null;
    if (type.equalsIgnoreCase("client")) return CompanyType.CLIENT;
    if (type.equalsIgnoreCase("provider")) return CompanyType.PROVIDER;
    return null;
  }
  
  public Currency converStringToCurrency(String currency){
    if (currency == null) return null;
    if (currency.equalsIgnoreCase("eur")) return Currency.EUR;
    if (currency.equalsIgnoreCase("usd")) return Currency.USD;
    return null;
  }
  
  public InvoiceStatus convertStringToInvoiceStatus(String status){
    if (status == null) return null;
    if (status.equalsIgnoreCase("open")) return InvoiceStatus.OPEN;
    if (status.equalsIgnoreCase("paid")) return InvoiceStatus.PAID;
    return null;
  }
  
  public InvoiceType convertStringToInvoiceType (String type){
    if (type == null) return null;
    if (type.equalsIgnoreCase("incoming")) return InvoiceType.INCOMING;
    if (type.equalsIgnoreCase("outgoing")) return InvoiceType.OUTGOING;
    return null;
  }
}
