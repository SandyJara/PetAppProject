package com.project.PetAppSandra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.PetAppSandra.Experience;
import com.project.PetAppSandra.SitterPreferences;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.ExperienceRepository;
import com.project.PetAppSandra.repository.PreferencesRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private ExperienceRepository experienceRepository;

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
    
    //get and read the information of the current user
    @GetMapping("/preferences")
    public String getPreferencesPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("sitterId", user.getId());
        }
        return "preferences";
    }
    
    
    @GetMapping("/{sitterId}")
    public ResponseEntity<SitterPreferences> getPreferences(@PathVariable Long sitterId) {
        Optional<SitterPreferences> preferences = preferencesRepository.findBySitterId(sitterId);

        if (preferences.isPresent()) {
            return ResponseEntity.ok(preferences.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    //for the search of pet Sitters by their preferences
    @GetMapping("/search")
    public List<Map<String, Object>> searchSittersByServiceTypeAndStatus(@RequestParam String serviceType,
                                                                          @RequestParam String statusProfile) {
        
        List<Object[]> results = preferencesRepository.findSittersWithNameByServiceTypeAndStatusProfile(serviceType, statusProfile);

        // format for the information 
        return results.stream().map(result -> {
            Map<String, Object> sitterData = new HashMap<>();
            sitterData.put("sitterId", result[0]); // Pet sitter ID
            sitterData.put("username", result[1]); // Pet sitter name
            return sitterData;
        }).collect(Collectors.toList());
    }
    
    
    
}