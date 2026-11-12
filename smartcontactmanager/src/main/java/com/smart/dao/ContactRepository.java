package com.smart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findAllByUser_IdOrderByContactNameAsc(int userId);

    Optional<Contact> findByCIdAndUser_Id(int contactId, int userId);
}
