package org.thibault.cogiprestapi.services;

import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.model.Invoice;
import org.thibault.cogiprestapi.repositories.InvoiceRepository;

import java.util.List;

@Service
public class InvoiceService {
  
  private final InvoiceRepository invoiceRepository;
  
  public InvoiceService(InvoiceRepository invoiceRepository){
    this.invoiceRepository = invoiceRepository;
  }
  
  public List<Invoice> getAllInvoices(){
    return this.invoiceRepository.getAllInvoices();
  }
  
  public List<Invoice> searchInvoicesByFilters(Integer id, Integer companyId, String invoiceNumber, String type, String status){
    return this.invoiceRepository.searchInvoicesByFilters(id, companyId, invoiceNumber, type, status);
  }
  
  public void addInvoice(Invoice invoice){
    this.invoiceRepository.addInvoice(invoice);
  }
  
}
