package com.project.PetAppSandra.repository;

import com.google.cloud.storage.Acl.User;
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
    
  //added to get completed services for the rating part
    @Query("SELECT s FROM Service s WHERE s.status = 'COMPLETED'")
    List<Service> findCompletedServices();
   
  //added to get the name of the pet and the pet sitter, I have in my table only the pet sitter ID for the reviews
    @Query("SELECT s.id, s.serviceType, p.name AS petName, u.username AS sitterUsername, s.startDate, s.endDate, s.status, s.description, s.payment " +
    		"FROM Service s " +
    		"LEFT JOIN User u ON s.sitterId = u.id " +
    		"LEFT JOIN Pet p ON s.petId = p.id " +
    		"WHERE s.ownerId = :ownerId")
    	List<Object[]> findServicesWithPetAndSitterUsernameByOwnerId(@Param("ownerId") Long ownerId);

   //added to get the name of the pet sitter for reviews
    	@Query("SELECT DISTINCT u.id, u.username FROM Service s " +
    		       "JOIN User u ON s.sitterId = u.id " +
    		       "WHERE s.ownerId = :ownerId AND s.status = 'COMPLETED'")
    		List<Object[]> findSittersByOwnerId(@Param("ownerId") Long ownerId);
    
}
