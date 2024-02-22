package org.thibault.cogiprestapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thibault.cogiprestapi.model.Contact;
import org.thibault.cogiprestapi.services.ContactService;

import java.util.List;

@RestController
@RequestMapping
public class ContactController {
  
  private final ContactService contactService;
  
  public ContactController(ContactService contactService){
    this.contactService = contactService;
  }
  
  @GetMapping ("/contacts")
  public List<Contact> getAllContact() {
    return this.contactService.getAllContacts();
  }
}
