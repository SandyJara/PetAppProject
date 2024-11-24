package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get the reviews from a petsitter 
    @Query("SELECT r FROM Review r WHERE r.sitterId = :sitterId")
    List<Review> findBySitterId(@Param("sitterId") Long sitterId);
}