package com.project.PetAppSandra.repository;
import com.project.PetAppSandra.ContactUs;

import org.springframework.data.jpa.repository.JpaRepository;
// this interface is to manage all the related with the table CONTACT US that I also was created in sql
public interface ContactMessageRepository extends JpaRepository<ContactUs, Long> {
}
