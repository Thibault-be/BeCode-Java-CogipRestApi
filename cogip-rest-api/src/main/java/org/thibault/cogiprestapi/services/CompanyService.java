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
  
}
