package org.thibault.cogiprestapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.thibault.cogiprestapi.dto.ContactDTO;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.repositories.ContactRepository;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
  
  @Mock
  ContactRepository contactRepository;
  
  @InjectMocks
  ContactService contactService;
  
  @Test
  void getAllInvoicesEmptyListThrowsResultSetEmptyException(){
    given(contactRepository.getAllContacts()).willReturn(Collections.emptyList());
    assertThrows(ResultSetEmptyException.class, ()->{
      contactService.getAllContacts();
    });
  }
  
  @Test
  void idNotFoundReturnsIdNotFoundException(){
    int id = 5;
    given(contactRepository.getContactById(id)).willThrow(EmptyResultDataAccessException.class);
    assertThrows(IdNotFoundException.class, () ->{
      contactService.getContactById(id);
    });
  }
  
  @Test
  void getContactByFiltersNoHitsThrowsException(){
    String lastname = "thibault";
    given(contactRepository.getContactsByFilters(null, lastname, null, null, null)).willReturn(Collections.emptyList());
    
    assertThrows(ResultSetEmptyException.class, () -> {
            contactService.getContactsByFilters(null, lastname, null, null, null);
    });
  }
  
  
}
