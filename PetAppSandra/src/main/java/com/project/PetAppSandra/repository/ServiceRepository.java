package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    // Consult services BY OWNER
    List<Service> findByOwnerId(Long ownerId);

    // Consult services BY PET SITTER
    List<Service> findBySitterId(Long sitterId);

    // Consult services BY STATUS
    List<Service> findByStatus(String status);
    
    //added to get the name of the pet, I have in my table only the pet ID
    @Query("SELECT s.id, s.serviceType, p.name AS petName, s.startDate, s.endDate, s.status, s.description, s.payment FROM Service s LEFT JOIN Pet p ON s.petId = p.id WHERE s.ownerId = :ownerId")
    List<Object[]> findServicesWithPetNameByOwnerId(@Param("ownerId") Long ownerId);

}
