package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
