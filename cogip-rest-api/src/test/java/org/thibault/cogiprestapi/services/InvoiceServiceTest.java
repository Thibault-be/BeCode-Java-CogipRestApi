package org.thibault.cogiprestapi.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thibault.cogiprestapi.exceptions.*;
import org.thibault.cogiprestapi.model.Invoice;
import org.thibault.cogiprestapi.repositories.InvoiceRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
  
  @Mock
  InvoiceRepository invoiceRepository;
  
  @InjectMocks
  InvoiceService invoiceService;
  
  @Test
  void getAllInvoicesEmptyListThrows(){
    given(invoiceRepository.getAllInvoices()).willReturn(Collections.emptyList());
    assertThrows(ResultSetEmptyException.class, () ->{
      invoiceService.getAllInvoices();
    });
  }
  
  @Test
  void idNotFoundThrowsException(){
    given(invoiceRepository.getInvoiceById(5)).willThrow(EmptyResultDataAccessException.class);
    assertThrows(IdNotFoundException.class, () ->{
      invoiceService.getInvoiceById(5);
    });
  }
  
  @Test
  void noResultsForFiltersThrowsResultSetEmptyException(){
    given(invoiceRepository.searchInvoicesByFilters(null, "Thibault", null, null, null, null, null))
            .willReturn(Collections.emptyList());
    
    assertThrows(ResultSetEmptyException.class, () ->{
      invoiceService.searchInvoicesByFilters(null, "Thibault", null, null, null, null, null);
    });
  }
  
  @Test
  void invoiceNotCompleteThrowsMissingParametersException(){
    Invoice invoice = new Invoice();
    assertThrows(ParametersMissingException.class, () ->{
      invoiceService.addInvoice(invoice);
    });
  }
  
  @Test
  void updateInvoiceToExistingInvoiceNumberThrowsDuplicateValueException(){
    int id = 5;
    Invoice invoice = new Invoice();
    
    given(invoiceRepository.updateInvoice(id,invoice)).willThrow(DuplicateKeyException.class);
    
    assertThrows(DuplicateValueException.class, () ->{
      invoiceService.updateInvoice(id, invoice);
    });
  }
}
