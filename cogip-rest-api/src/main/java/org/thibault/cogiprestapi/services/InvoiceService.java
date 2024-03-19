package org.thibault.cogiprestapi.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.dto.InvoiceDTO;
import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;
import org.thibault.cogiprestapi.enums.InvoiceType;
import org.thibault.cogiprestapi.exceptions.*;
import org.thibault.cogiprestapi.model.Invoice;
import org.thibault.cogiprestapi.repositories.InvoiceRepository;
import java.util.List;

@Service
public class InvoiceService {
  
  private final InvoiceRepository invoiceRepository;
  
  public InvoiceService(InvoiceRepository invoiceRepository){
    this.invoiceRepository = invoiceRepository;
  }
  
  public List<InvoiceDTO> getAllInvoices(){
    List <InvoiceDTO> allInvoices = this.invoiceRepository.getAllInvoices();
    if (allInvoices.isEmpty()) throw new ResultSetEmptyException("No invoices were found.");
    return allInvoices;
  }
  
  public Invoice getInvoiceById(int id){
    try{
      return this.invoiceRepository.getInvoiceById(id);
    } catch (EmptyResultDataAccessException ex){
      throw new IdNotFoundException("Invoice with id " + id + " does not exist.");
    }
  }
  
  public List<Invoice> searchInvoicesByFilters(Integer id, Integer companyId, String invoiceNumber, Currency currency, InvoiceType type, InvoiceStatus status){
    List<Invoice> filteredInvoices = this.invoiceRepository.searchInvoicesByFilters(id, companyId, invoiceNumber, currency, type, status);
    if (filteredInvoices.isEmpty()) throw new ResultSetEmptyException("No invoices were found for your filters.");
    return filteredInvoices;
  }
  
  public Invoice addInvoice(Invoice invoice){
    String missingParams = missingParameters(invoice);
    if (missingParams != null) throw new ParametersMissingException(missingParams);
    
    try {
      return this.invoiceRepository.addInvoice(invoice);
    } catch (DuplicateKeyException ex){
      throw new DuplicateValueException("There is already an invoice with invoice number " + invoice.getInvoiceNumber() + " in the system.");
    }
    catch (DataIntegrityViolationException e){
      throw new IllegalParametersException("You've entered illegal parameters. Please review your data.");
    }
  }
  
  public Invoice updateInvoice(int id, Invoice invoice){
    getInvoiceById(id);
    try {
      return this.invoiceRepository.updateInvoice(id, invoice);
    } catch (DuplicateKeyException dke){
      throw new DuplicateValueException("Invoice with id " + id + " cannot be updated. " +
              "An invoice with invoice number " + invoice.getInvoiceNumber() + " already exists.");
    } catch (DataIntegrityViolationException dive){
      throw new IllegalParametersException("You've entered illegal parameters. Please review your data.");
    }
  }
  
  private String missingParameters(Invoice invoice){
    StringBuilder params = new StringBuilder();
    params.append("These are the missing parameters:\n");
    boolean flag = false;
    
    if (invoice.getCompanyId() == null){
      params.append("company_id\n");
      flag = true;
    }
    if (invoice.getContactId() == null){
      params.append("contact_id\n");
      flag = true;
    }
    if (invoice.getInvoiceNumber() == null){
      params.append("invoice_number\n");
      flag = true;
    }
    if (invoice.getValue() == null){
      params.append("value\n");
      flag = true;
    }
    if (invoice.getCurrency() == null){
      params.append("currency\n");
      flag = true;
    }
    if (invoice.getType() == null){
      params.append("type\n");
      flag = true;
    }
    if (invoice.getStatus() == null){
      params.append("status");
      flag = true;
    }
    if(flag) return params.toString();
    return null;
  }
}
