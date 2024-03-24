package org.thibault.cogiprestapi.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thibault.cogiprestapi.services.CompanyService;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {
  
  @Mock
  private CompanyService companyService;
  
  @InjectMocks
  private CompanyController companyController;
  
  @Test
  @DisplayName("Verifies is companyService")
  void getAllCompaniesHappyFlow(){
    companyController.getAllCompanies();
    verify(companyService).getAllCompanies();
  }
  
  @Test
  @DisplayName("Verifies if user not found IdNotFoundException thrown")
  void getUserByIdHappyFlow(){
    int id = 666;
    companyController.getCompanyById(id);
    verify(companyService).getCompanyById(id);
  }
}
