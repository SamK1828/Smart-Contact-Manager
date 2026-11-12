package com.smart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findAllByUser_IdOrderByContactNameAsc(int userId);

    @Query("""
            select c from Contact c
            where c.user.id = :userId
              and (:favoritesOnly = false or c.favorite = true)
              and (:tag is null or concat(',', lower(coalesce(c.tags, '')), ',')
                    like concat('%,', concat(lower(:tag), ',%')))
              and (:query is null
                   or lower(c.contactName) like concat('%', lower(:query), '%')
                   or lower(coalesce(c.secondName, '')) like concat('%', lower(:query), '%')
                   or lower(coalesce(c.phone, '')) like concat('%', lower(:query), '%')
                   or lower(coalesce(c.work, '')) like concat('%', lower(:query), '%')
                   or lower(coalesce(c.description, '')) like concat('%', lower(:query), '%')
                   or lower(coalesce(c.tags, '')) like concat('%', lower(:query), '%'))
            """)
    List<Contact> searchForUser(
            @Param("userId") int userId,
            @Param("query") String query,
            @Param("favoritesOnly") boolean favoritesOnly,
            @Param("tag") String tag,
            Sort sort);

    Optional<Contact> findByCIdAndUser_Id(int contactId, int userId);
}
