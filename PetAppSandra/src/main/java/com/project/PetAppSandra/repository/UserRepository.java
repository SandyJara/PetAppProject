package com.project.PetAppSandra.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.PetAppSandra.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email); //because I want to allow they can use both options to log in
}