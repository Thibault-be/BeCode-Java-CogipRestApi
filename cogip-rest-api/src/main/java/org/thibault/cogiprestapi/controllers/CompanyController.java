package org.thibault.cogiprestapi.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.exceptions.DuplicateValueException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
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
          @RequestParam (required = false) Integer id,
          @RequestParam (required = false) String name,
          @RequestParam (required = false) String country,
          @RequestParam (required = false) String vat,
          @RequestParam (required = false) CompanyType type
        ){
    return this.companyService.searchCompaniesByFilters(id, name, country, vat, type);
  }
  
  @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
  @PostMapping ("/companies/add")
  public ResponseEntity<Company> addCompany(@RequestBody Company company){
    try{
      return ResponseEntity.ok(this.companyService.addCompany(company));
    } catch (DuplicateKeyException e){
      throw new DuplicateValueException("A company with this VAT number already exists in the table");
    }
  }
  
  @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
  @DeleteMapping ("/companies/{id}")
  public ResponseEntity<String> deleteCompany(@PathVariable int id){
    this.companyService.deleteCompany(id);
    return ResponseEntity.ok("Company with company_id " + id + " was successfully deleted.");
  }
  
  @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
  @PutMapping ("/companies/{id}")
  public ResponseEntity<Company> updateCompany(@PathVariable int id,
                                              @RequestBody Company company){
    Company updatedCompany = this.companyService.updateCompany(id, company);
    
    return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(updatedCompany);
  }
}
