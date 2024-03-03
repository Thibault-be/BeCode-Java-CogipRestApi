package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.enums.CompanyType;
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
  
  @GetMapping ("/companies/{id}")
  public Company getCompanyById(@PathVariable("id") int id){
    return this.companyService.getCompanyById(id);
  }
  
  @GetMapping ("/companies/search")
  public List<Company> searchCompaniesByFilters(
          @RequestParam (required = false) String id,
          @RequestParam (required = false) String name,
          @RequestParam (required = false) String country,
          @RequestParam (required = false) String vat,
          @RequestParam (required = false) CompanyType type
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
  
  @PutMapping ("/companies/{id}")
  public ResponseEntity<Company> updateCompany(@PathVariable int id,
                                              @RequestBody Company company){
    Company updatedCompany = this.companyService.updateCompany(id, company);
    
    return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(updatedCompany);
  }
}
