package com.project.PetAppSandra.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.PetAppSandra.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class SitterController {

    @GetMapping("/petSitter")
    public String petSitterProfile(Model model, HttpSession session) {
        // get the usser logged
        User user = (User) session.getAttribute("user");

        // Verify if is logged and Pet Sitter
        if (user != null && "PETSITTER".equalsIgnoreCase(user.getAccount().toString())) {
            model.addAttribute("user", user); //adds the user to the model Thymeleaf
            return "petSitter"; 
        } else {
            return "redirect:/login"; // if not logged, goes to thet page
        }
    }

    @GetMapping("/petSitter/data")
    public ResponseEntity<?> getPetSitterData(HttpSession session) {
        // gets the user logged
        User user = (User) session.getAttribute("user");

        // Verify if is logged and Pet Sitter
        if (user != null && "PETSITTER".equalsIgnoreCase(user.getAccount().toString())) {
            Map<String, Object> data = new HashMap<>();
            data.put("fullname", user.getFullname());
            data.put("email", user.getEmail());
            data.put("phone", user.getPhone());
            data.put("birthdate", user.getBirthdate());
            data.put("address", user.getAddress());
            data.put("username", user.getUsername());
            data.put("profilePictureUrl", user.getProfilePictureUrl()); 

            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(401).body("Unauthorized: Please log in again.");
        }
    }
}
