package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.dto.InvoiceDTO;
import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;
import org.thibault.cogiprestapi.enums.InvoiceType;
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
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_INTERN')")
  @GetMapping ("/invoices")
  public List<InvoiceDTO> getAllInvoices(){
    return this.invoiceService.getAllInvoices();
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_INTERN')")
  @GetMapping("invoices/{id}")
  public Invoice getInvoiceById(@PathVariable("id") int id){
    return this.invoiceService.getInvoiceById(id);
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_INTERN')")
  @GetMapping ("/invoices/search")
  public List<InvoiceDTO> searchInvoicesByFilters(
          @RequestParam (required = false) Integer id,
          //@RequestParam (required = false) Integer companyId,
          @RequestParam (required = false) String companyName,
          @RequestParam (required = false) String invoiceNumber,
          @RequestParam (required = false) Currency currency,
          @RequestParam (required = false) InvoiceType type,
          @RequestParam (required = false) InvoiceStatus status,
          @RequestParam (required = false) String contactName
  ){
    return this.invoiceService.searchInvoicesByFilters(id, companyName, invoiceNumber, currency, type, status, contactName);
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT')")
  @PostMapping ("/invoices")
  public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice){
    Invoice invoiceToReturn = this.invoiceService.addInvoice(invoice);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(invoiceToReturn);
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT')")
  @PutMapping ("invoices/{id}")
  public ResponseEntity<Invoice> updateInvoice(
                                      @PathVariable int id,
                                      @RequestBody Invoice invoice){
    Invoice updatedInvoice = this.invoiceService.updateInvoice(id, invoice);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedInvoice);
  }
}
