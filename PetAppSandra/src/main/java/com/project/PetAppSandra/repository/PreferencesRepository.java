package com.project.PetAppSandra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.PetAppSandra.SitterPreferences;

@Repository
public interface PreferencesRepository extends JpaRepository<SitterPreferences, Long> {
    Optional<SitterPreferences> findBySitterId(Long sitterId);
    
    List<SitterPreferences> findAllBySitterId(Long sitterId);
    
 // to find by service Type and status Profile
    @Query("SELECT s.sitterId, u.username " +
    	       "FROM SitterPreferences s " +
    	       "JOIN User u ON s.sitterId = u.id " +
    	       "WHERE s.serviceType LIKE %:serviceType% AND s.statusProfile = :statusProfile")
    	List<Object[]> findSittersWithNameByServiceTypeAndStatusProfile(
    	    @Param("serviceType") String serviceType,
    	    @Param("statusProfile") String statusProfile
    	);
}
