package org.thibault.cogiprestapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thibault.cogiprestapi.model.Invoice;
import org.thibault.cogiprestapi.services.InvoiceService;

import java.util.List;

@RestController
@RequestMapping
public class InvoiceController {
  
  private final InvoiceService invoiceService;
  
  public InvoiceController(InvoiceService invoiceService){
    this.invoiceService = invoiceService;
  }
  
  @GetMapping ("/invoices")
  public List<Invoice> getAllInvoices(){
    return this.invoiceService.getAllInvoices();
  }
}
