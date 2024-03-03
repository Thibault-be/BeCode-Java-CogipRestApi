package org.thibault.cogiprestapi.services;

import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.model.Contact;
import org.thibault.cogiprestapi.repositories.ContactRepository;

import java.util.List;

@Service
public class ContactService {
  
  private final ContactRepository contactRepository;
  
  public ContactService(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }
  
  public List<Contact> getAllContacts(){
    return this.contactRepository.getAllContacts();
  }
  
  public Contact getContactById(int id){
    return this.contactRepository.getContactById(id);
  }
  
  public List<Contact> getContactsByFilters(Integer id, String firstname, String lastname, String phone, Integer companyId){
    return this.contactRepository.getContactsByFilters(id, firstname, lastname, phone, companyId);
  }
  
  public void addContact(Contact contact){
    this.contactRepository.addContact(contact);
  }
  
  public Contact updateContact(Contact contact){
    return this.contactRepository.updateContact(contact);
  }
  
  public void deleteContact(int id){
    this.contactRepository.deleteContact(id);
  }
}
