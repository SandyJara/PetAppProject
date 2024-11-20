package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;


// this interface is to manage all the related with the table contact_message that I also was created in sql
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

}