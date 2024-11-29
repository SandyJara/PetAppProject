package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Optional<Experience> findBySitterId(Long sitterId);
}