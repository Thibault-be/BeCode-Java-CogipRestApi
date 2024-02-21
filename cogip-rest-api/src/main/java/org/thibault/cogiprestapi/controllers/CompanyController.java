package org.thibault.cogiprestapi.controllers;

import org.springframework.web.bind.annotation.*;
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
  
  @GetMapping ("/companies/search")
  public List<Company> searchCompaniesByFilters(
          @RequestParam (required = false) String id,
          @RequestParam (required = false) String name,
          @RequestParam (required = false) String country,
          @RequestParam (required = false) String vat,
          @RequestParam (required = false) String type
        ){
    return this.companyService.searchCompaniesByFilters(id, name, country, vat, type);
  }
  
  @PostMapping ("/companies/add")
  public String addCompany(@RequestBody Company company){
    this.companyService.addCompany(company);
    
    return "Company " + company.getName() + " successfully added.";
  }
}
