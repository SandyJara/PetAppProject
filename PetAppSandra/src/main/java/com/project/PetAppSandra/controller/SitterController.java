package com.project.PetAppSandra.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SitterController {
	
	 @Autowired
	    private UserRepository userRepository;
	    

	    @GetMapping("/petSitter")
	    public String petSitterProfile(Model model, HttpSession session) {
	        User user = (User) session.getAttribute("user");

	        // logged and  Pet Sitter.
	        if (user != null && "PETSITTER".equalsIgnoreCase(user.getAccount().toString())) {
	            model.addAttribute("user", user); 
	            return "petSitter"; 
	        }

	        // if not, goes to login.
	        return "redirect:/login";
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
    
    
/////////new for the updateOwner page
    
@PostMapping("/updateSitter")
public ResponseEntity<String> updateOwnerProfile(@RequestBody Map<String, String> updateData, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user != null && "PETSITTER".equalsIgnoreCase(user.getAccount().name())) {
        String phone = updateData.get("phone");
        String address = updateData.get("address");
        String newPassword = updateData.get("newPassword");

        // update this information, not all registered from the begining can be changed
        user.setPhone(phone);
        user.setAddress(address);

        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }

        userRepository.save(user);
        session.setAttribute("user", user);
        return ResponseEntity.ok("Profile updated successfully");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
}



@GetMapping("/updatePetSitter/data")
public ResponseEntity<Map<String, Object>> getOwnerProfileData(HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user != null && "PETSITTER".equalsIgnoreCase(user.getAccount().name())) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("fullname", user.getFullname());
        userData.put("phone", user.getPhone());
        userData.put("address", user.getAddress());
        userData.put("email", user.getEmail());
        userData.put("birthdate", user.getBirthdate());
        userData.put("username", user.getUsername());
        userData.put("profilePictureUrl", user.getProfilePictureUrl());

	        return ResponseEntity.ok(userData);
	    } else if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", "No user found in session. Please log in."));
	    } else {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("error", "You are not authorized to view this page."));
	    }
	}
    
    
}
