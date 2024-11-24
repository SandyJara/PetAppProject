package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.SitterAvailability;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitterAvailabilityRepository extends JpaRepository<SitterAvailability, Long> {
    
        List<SitterAvailability> findBySitterId(Long sitterId);
}
