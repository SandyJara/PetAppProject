package com.project.PetAppSandra.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.PetAppSandra.Pet;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.PetRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    // This part is to gett all the pets that have been already registered by an user
    @GetMapping
    public ResponseEntity<List<Pet>> getPetsByOwner(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Pet> pets = petRepository.findByOwnerId(user.getId());
        return ResponseEntity.ok(pets);
    }

    // This is to get the data from a specific Pet when the user selects one from the availables 
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent() && pet.get().getOwnerId().equals(user.getId())) {
            return ResponseEntity.ok(pet.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // This part is to create or update a specific pet
    @PostMapping
    public ResponseEntity<String> saveOrUpdatePet(@RequestBody Pet pet, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        if (pet.getId() == null) {
            // To create
            pet.setOwnerId(user.getId());
            petRepository.save(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pet created successfully");
        } else {
            // To update 
            Optional<Pet> existingPet = petRepository.findById(pet.getId());
            if (existingPet.isPresent() && existingPet.get().getOwnerId().equals(user.getId())) {
                pet.setOwnerId(user.getId());
                petRepository.save(pet);
                return ResponseEntity.ok("Pet updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to update this pet");
            }
        }
    }

    // Delete information from a pet, once a pet has been selected first
    @DeleteMapping("/{petId}")
    public ResponseEntity<String> deletePet(@PathVariable Long petId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent() && pet.get().getOwnerId().equals(user.getId())) {
            petRepository.delete(pet.get());
            return ResponseEntity.ok("Pet deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to delete this pet");
        }
    }
}
