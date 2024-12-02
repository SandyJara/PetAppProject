package com.project.PetAppSandra.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.PetAppSandra.Service;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.User.AccountType;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email); //because I want to allow they can use both options to log in
    Optional<User> findByEmail(String email);//this added for the contact messages
    //for the messages
    boolean existsByUsername(String username);
    
    //for google maps with the services from the owners
    @Query("SELECT u.username, u.address FROM User u WHERE u.account = 'owner'")
    List<Object[]> findAllOwnerAddresses();
  //  List<Service> findByStatus(String status);
    List<User> findByAccount(AccountType account);
}