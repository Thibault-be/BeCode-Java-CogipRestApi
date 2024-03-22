package org.thibault.cogiprestapi.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;
import org.thibault.cogiprestapi.enums.InvoiceType;
import org.thibault.cogiprestapi.exceptions.*;
import org.thibault.cogiprestapi.model.Invoice;
import org.thibault.cogiprestapi.repositories.InvoiceRepository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.thibault.cogiprestapi.dto.CompanyDTO;
import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.model.Company;
import org.thibault.cogiprestapi.repositories.CompanyRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import static org.mockito.Mockito.*;
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
  void invoiceWithIllegalParametersException(){
    Invoice invoice = new Invoice(null, 2,2,"456", new BigDecimal("1000.00"), Currency.USD, InvoiceType.INCOMING, InvoiceStatus.OPEN);
    
    given(invoiceRepository.addInvoice(new Invoice())).willThrow(DataIntegrityViolationException.class);
    
    assertThrows(IllegalParametersException.class, () -> {
      invoiceService.addInvoice(invoice);
    });
  }
  
  
}
