package org.thibault.cogiprestapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thibault.cogiprestapi.model.Company;
import org.thibault.cogiprestapi.services.CompanyService;

import java.util.List;

@RestController
@RequestMapping
public class CompanyController {
  
  private final CompanyService companyService;
  
  public CompanyController(CompanyService companyService){
    this.companyService = companyService;
  }
  
  @GetMapping ("/companies")
  public List<Company> getAllCompanies(){
    return this.companyService.getAllCompanies();
  }
}
