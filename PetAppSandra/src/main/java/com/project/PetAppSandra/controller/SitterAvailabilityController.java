package com.project.PetAppSandra.controller;

import com.project.PetAppSandra.SitterAvailability;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.SitterAvailabilityRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class SitterAvailabilityController {

    @Autowired
    private SitterAvailabilityRepository repository;

    
    @PostMapping("/create")
    public ResponseEntity<String> createAvailability(@RequestBody SitterAvailability availability, HttpSession session) {
        // Obtener el usuario logueado
        User user = (User) session.getAttribute("user");
        if (user == null || !"PET_SITTER".equalsIgnoreCase(user.getAccount().toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only Pet Sitters can create availability.");
        }

        // Asignar el ID del Pet Sitter a la disponibilidad
        availability.setSitterId(user.getId());
        repository.save(availability);

        return ResponseEntity.ok("Availability created successfully!");
    }

    @GetMapping("/list")
    public ResponseEntity<List<SitterAvailability>> getAvailabilityBySitter(HttpSession session) {
        // Obtener el usuario logueado
        User user = (User) session.getAttribute("user");
        if (user == null || !"PET_SITTER".equalsIgnoreCase(user.getAccount().toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Consultar las disponibilidades del Pet Sitter
        List<SitterAvailability> availabilities = repository.findBySitterId(user.getId());
        return ResponseEntity.ok(availabilities);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAvailability(@PathVariable Long id, HttpSession session) {
        // Obtener el usuario logueado
        User user = (User) session.getAttribute("user");
        if (user == null || !"PET_SITTER".equalsIgnoreCase(user.getAccount().toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only Pet Sitters can delete availability.");
        }

        // Verificar que la disponibilidad pertenece al Pet Sitter
        SitterAvailability availability = repository.findById(id).orElse(null);
        if (availability == null || !availability.getSitterId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this availability.");
        }

        // Eliminar la disponibilidad
        repository.delete(availability);
        return ResponseEntity.ok("Availability deleted successfully!");
    }
    
    
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAvailability(
            @PathVariable Long id,
            @RequestBody SitterAvailability updatedAvailability,
            HttpSession session) {
        // Obtener el usuario logueado
        User user = (User) session.getAttribute("user");
        if (user == null || !"PET_SITTER".equalsIgnoreCase(user.getAccount().toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only Pet Sitters can update availability.");
        }

        // Verificar que la disponibilidad pertenece al Pet Sitter
        SitterAvailability existingAvailability = repository.findById(id).orElse(null);
        if (existingAvailability == null || !existingAvailability.getSitterId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this availability.");
        }

        // Actualizar los campos permitidos
        existingAvailability.setServiceType(updatedAvailability.getServiceType());
        existingAvailability.setPreferredSpecies(updatedAvailability.getPreferredSpecies());
        existingAvailability.setPreferredSize(updatedAvailability.getPreferredSize());
        existingAvailability.setStartDate(updatedAvailability.getStartDate());
        existingAvailability.setEndDate(updatedAvailability.getEndDate());
        existingAvailability.setArea(updatedAvailability.getArea());
        
        repository.save(existingAvailability);

        return ResponseEntity.ok("Availability updated successfully!");
    }
}
