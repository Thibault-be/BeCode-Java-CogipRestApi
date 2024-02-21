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
  
}
