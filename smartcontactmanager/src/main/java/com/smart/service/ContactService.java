package com.smart.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.models.Contact;
import com.smart.models.ContactForm;
import com.smart.models.User;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public List<Contact> findContacts(UserDetails principal) {
        User user = currentUser(principal);
        return contactRepository.findAllByUser_IdOrderByContactNameAsc(user.getId());
    }

    public Contact findContact(UserDetails principal, int contactId) {
        User user = currentUser(principal);
        return contactRepository.findByCIdAndUser_Id(contactId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Contact createContact(UserDetails principal, ContactForm form) {
        Contact contact = new Contact();
        copyForm(form, contact);
        contact.setUser(currentUser(principal));
        return contactRepository.save(contact);
    }

    public Contact updateContact(UserDetails principal, int contactId, ContactForm form) {
        Contact contact = findContact(principal, contactId);
        copyForm(form, contact);
        return contactRepository.save(contact);
    }

    public void deleteContact(UserDetails principal, int contactId) {
        contactRepository.delete(findContact(principal, contactId));
    }

    private User currentUser(UserDetails principal) {
        return userRepository.findByEmailIgnoreCase(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void copyForm(ContactForm form, Contact contact) {
        contact.setContactName(form.getContactName().trim());
        contact.setSecondName(form.getSecondName() == null ? "" : form.getSecondName().trim());
        contact.setWork(form.getWork() == null ? "" : form.getWork().trim());
        contact.setPhone(form.getPhone().trim());
        contact.setDescription(form.getDescription() == null ? "" : form.getDescription().trim());
    }
}
