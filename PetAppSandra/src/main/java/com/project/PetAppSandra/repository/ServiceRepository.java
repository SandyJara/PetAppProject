package com.project.PetAppSandra.repository;

import com.google.cloud.storage.Acl.User;
import com.project.PetAppSandra.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    		
    		
    		
    		
   /// still testing-table when a pet sitter search services
    		@Query("SELECT s.id, s.serviceType, p.name AS petName, u.id AS ownerId, u.fullname AS ownerName, " +
    			       "s.startDate, s.endDate, s.payment, s.description " +
    			       "FROM Service s " +
    			       "LEFT JOIN User u ON s.ownerId = u.id " +
    			       "LEFT JOIN Pet p ON s.petId = p.id " +
    			       "WHERE s.status = 'PENDING' " +
    			       "AND (:serviceType IS NULL OR s.serviceType = :serviceType)")
    			List<Object[]> findPendingServicesWithOwnerAndPet(@Param("serviceType") String serviceType);


    		//Check if this is no needed
    			@Query("SELECT s.id, s.serviceType, s.status, u.username AS sitterUsername, s.description, s.startDate, s.endDate " +
    				       "FROM Service s " +
    				       "LEFT JOIN User u ON s.sitterId = u.id " +
    				       "WHERE s.id = :serviceId")
    				Optional<Object[]> findServiceWithSitterName(@Param("serviceId") Long serviceId);
    				
   //added to allow pet sitter to consult their services
    				@Query("SELECT s.id AS serviceId, s.serviceType AS serviceType, " +
    					       "p.name AS petName, u.fullname AS ownerName, " +
    					       "s.startDate AS startDate, s.endDate AS endDate, " +
    					       "s.payment AS payment, s.description AS description, " +
    					       "s.status AS status, s.hiddenForSitter AS hiddenForSitter, s.hiddenForOwner AS hiddenForOwner " +
    					       "FROM Service s " +
    					       "LEFT JOIN User u ON s.ownerId = u.id " +
    					       "LEFT JOIN Pet p ON s.petId = p.id " +
    					       "WHERE s.sitterId = :sitterId " +
    					       "AND s.status IN ('APPLIED', 'ACCEPTED', 'COMPLETED', 'CANCELLED')")
    					List<Object[]> findDetailedServicesBySitterId(@Param("sitterId") Long sitterId);
 
 //to hide from the list, the services the petsitter selects   					
    				@Query("SELECT s FROM Service s WHERE s.hiddenForSitter = false AND s.sitterId = :sitterId")
    				List<Service> findVisibleServicesForSitter(@Param("sitterId") Long sitterId);
}
