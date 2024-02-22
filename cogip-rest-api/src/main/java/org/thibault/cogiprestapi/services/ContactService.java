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
}
