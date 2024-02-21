package org.thibault.cogiprestapi.controllers;

import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> addCompany(@RequestBody Company company){
    this.companyService.addCompany(company);
    return ResponseEntity.ok("Company " + company.getName() + " successfully added.");
  }
  
  @DeleteMapping ("/companies/{id}")
  public ResponseEntity<String> deleteCompany(@PathVariable int id){
    this.companyService.deleteCompany(id);
    
    return ResponseEntity.ok("Company with company_id " + id + " was successfully deleted.");
  }
}
