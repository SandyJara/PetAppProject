package com.project.PetAppSandra.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.PetAppSandra.SitterPreferences;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.PreferencesRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private PreferencesRepository preferencesRepository;

    @PostMapping("/update")
    public ResponseEntity<?> updatePreferences(@RequestBody Map<String, Object> preferencesData, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }

        // the information
        List<String> serviceTypes = (List<String>) preferencesData.get("serviceTypes");//it could be 1 or more values
        List<String> petTypes = (List<String>) preferencesData.get("petTypes");//it could be 1 or more values
        String statusProfile = (String) preferencesData.get("statusProfile");

        // Find existing preferences or create new ones
        Optional<SitterPreferences> optionalPreferences = preferencesRepository.findBySitterId(user.getId());
        SitterPreferences preferences = optionalPreferences.orElse(new SitterPreferences());
        preferences.setSitterId(user.getId());

        // Update fields
        preferences.setServiceType(String.join(",", serviceTypes));
        preferences.setPetType(String.join(",", petTypes));
        preferences.setStatusProfile(statusProfile);

        // Save preferences
        preferencesRepository.save(preferences);

        return ResponseEntity.ok("Preferences updated successfully.");
    }
}