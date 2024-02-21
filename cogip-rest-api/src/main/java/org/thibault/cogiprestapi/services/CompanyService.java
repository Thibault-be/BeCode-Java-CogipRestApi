package org.thibault.cogiprestapi.services;

import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.model.Company;
import org.thibault.cogiprestapi.repositories.CompanyRepository;

import java.util.List;

@Service
public class CompanyService {
  
  private final CompanyRepository companyRepository;
  
  public CompanyService(CompanyRepository companyRepository){
    this.companyRepository = companyRepository;
  }
  
  public List<Company> getAllCompanies(){
    return this.companyRepository.getAllCompanies();
  }
  
  public List<Company> searchCompaniesByFilters(String id, String name, String country, String vat, String type){
    return this.companyRepository.searchCompaniesByFilters(id, name, country, vat, type);
  }
  
  public void addCompany(Company company){
    this.companyRepository.addCompany(company);
  }
  
  public void deleteCompany(int id){
    this.companyRepository.deleteCompany(id);
  }
  
}
