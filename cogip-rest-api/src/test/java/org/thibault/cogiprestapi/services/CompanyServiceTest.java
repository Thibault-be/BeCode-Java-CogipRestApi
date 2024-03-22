package org.thibault.cogiprestapi.services;

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
import org.thibault.cogiprestapi.exceptions.DuplicateValueException;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.ParametersMissingException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.model.Company;
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
  
  @Test
  void getCompanyByIdThrowsIdNotFoundException(){
    int id = 666;
    given(companyRepository.getCompanyById(id)).willThrow(EmptyResultDataAccessException.class);
    assertThrows(IdNotFoundException.class, () -> {
      companyService.getCompanyById(id);
    });
  }
  
  @Test
  void getCompaniesByFiltersEmptyList(){
    String name = "thibault";
    String country = "Latvia";
    given(companyRepository.searchCompaniesByFilters(null, name, country, null,null)).willReturn(Collections.emptyList());
    assertThrows(ResultSetEmptyException.class, () ->{
      companyService.searchCompaniesByFilters(null, name, country, null,null);
    });
  }
  
  @Test
  void parametersMissingException(){
    Company company = new Company();
    assertThrows(ParametersMissingException.class, () ->{
      companyService.addCompany(company);
    });
  }
  
  @Test
  void cannotAddCompanyWithSameName(){
    Company company = new Company("thibault", "Belgium", "1235", CompanyType.CLIENT);
    given(companyRepository.addCompany(company)).willThrow(DuplicateKeyException.class);
    assertThrows(DuplicateValueException.class, () ->{
      companyService.addCompany(company);
    });
  }
  
  @Test
  void cannotUpdateCompanyWhenIdNotFound(){
    int id = 666;
    Company company = new Company("thibault", "Belgium", "1235", CompanyType.CLIENT);
    given(companyRepository.updateCompany(id, company)).willThrow(EmptyResultDataAccessException.class);
    assertThrows(IdNotFoundException.class, () ->{
      companyService.updateCompany(id, company);
    });
  }
}
