package com.project.PetAppSandra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.PetAppSandra.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerId(Long ownerId);
}