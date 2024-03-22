package org.thibault.cogiprestapi.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thibault.cogiprestapi.dto.CompanyDTO;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.repositories.CompanyRepository;
import java.util.*;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
  
  @Mock
  CompanyRepository companyRepository;
  
  @InjectMocks
  CompanyService companyService;
  
  @Test
  void getAllCompaniesHappyFlow(){
    List<CompanyDTO> companies = Arrays.asList(new CompanyDTO(), new CompanyDTO());
    given(companyRepository.getAllCompanies()).willReturn(companies);
    companyService.getAllCompanies();
    verify(companyRepository).getAllCompanies();
  }
  
  @Test
  void getAllCompaniesEmptyList(){
    given(companyRepository.getAllCompanies()).willReturn(Collections.emptyList());
    assertThrows(ResultSetEmptyException.class, () ->{
      companyService.getAllCompanies();
    });
  }
  
}
