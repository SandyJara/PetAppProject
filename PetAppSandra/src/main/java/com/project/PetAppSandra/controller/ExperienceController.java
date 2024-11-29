package com.project.PetAppSandra.controller;

import com.project.PetAppSandra.Experience;
import com.project.PetAppSandra.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/experience")
public class ExperienceController {

    @Autowired
    private ExperienceRepository experienceRepository;

    //Get experience by the ID from the pet sitter
    @GetMapping("/{sitterId}")
    public ResponseEntity<?> getExperience(@PathVariable Long sitterId) {
        try {
            Optional<Experience> experience = experienceRepository.findBySitterId(sitterId);
            if (experience.isPresent()) {
                return ResponseEntity.ok(experience.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Experience not found for sitterId: " + sitterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting the experience: " + e.getMessage());
        }
    }

    // Make a new one
    @PostMapping
    public ResponseEntity<?> createExperience(@RequestBody Experience experience) {
        try {
            Experience savedExperience = experienceRepository.save(experience);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExperience);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating experience: " + e.getMessage());
        }
    }

    // Update one
    @PutMapping("/{sitterId}")
    public ResponseEntity<?> updateOrCreateExperience(
            @PathVariable Long sitterId, @RequestBody Experience experienceData) {
        try {
            Optional<Experience> existingExperience = experienceRepository.findBySitterId(sitterId);

            if (existingExperience.isPresent()) {
                // Updating one already existing
                Experience experience = existingExperience.get();
                experience.setExperienceText(experienceData.getExperienceText());
                experienceRepository.save(experience);
                return ResponseEntity.ok("Experience updated successfully");
            } else {
                // new one
                Experience newExperience = new Experience();
                newExperience.setSitterId(sitterId);
                newExperience.setExperienceText(experienceData.getExperienceText());
                experienceRepository.save(newExperience);
                return ResponseEntity.status(HttpStatus.CREATED).body("Experience created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating or creating experience: " + e.getMessage());
        }
    }
    
    // Delete
    @DeleteMapping("/{sitterId}")
    public ResponseEntity<?> deleteExperience(@PathVariable Long sitterId) {
        try {
            Optional<Experience> existingExperience = experienceRepository.findBySitterId(sitterId);

            if (existingExperience.isPresent()) {
                experienceRepository.delete(existingExperience.get());
                return ResponseEntity.ok("Experience deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Experience not found for sitterId: " + sitterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting experience: " + e.getMessage());
        }
    }
}
