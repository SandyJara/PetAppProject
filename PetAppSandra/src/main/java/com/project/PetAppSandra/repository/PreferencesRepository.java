package com.project.PetAppSandra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.PetAppSandra.SitterPreferences;

@Repository
public interface PreferencesRepository extends JpaRepository<SitterPreferences, Long> {
    Optional<SitterPreferences> findBySitterId(Long sitterId);
}
