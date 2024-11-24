package com.project.PetAppSandra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.UserRepository;
import com.project.PetAppSandra.repository.ServiceRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LogInController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
    
    
    @PostMapping("/login")
    public String login(@RequestParam String identifier, @RequestParam String password, Model model, HttpSession session) {
        try {
            Optional<User> userOptional = userRepository.findByUsernameOrEmail(identifier, identifier);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // some messages added to see if there is an issue for correct logIn
                System.out.println("User found: " + user.getUsername());

                if (user.getPassword() != null && user.getPassword().equals(password)) {
                    // if the password is correct
                    System.out.println("valid password");

                    // Save the user in the sesion
                    session.setAttribute("user", user);

                    // this is to show the next page if the logIn was correct and depends if its owner or petsitter, it compares it from sql database
                    if (user.getAccount() != null && user.getAccount().toString().equalsIgnoreCase("owner")) {
                        System.out.println("Redirecting to owner profile page");
                        return "redirect:/owner";
                    } else if (user.getAccount() != null && user.getAccount().toString().equalsIgnoreCase("petsitter")) {
                        System.out.println("Redirecting to pet sitter profile page");
                        return "redirect:/petSitter";
                    } else {
                        model.addAttribute("error", "Unexpected role.");
                        System.out.println("unexpected role: " + user.getAccount()); //since It reading this value from sql, show be not necessary this validation
                    }
                } else {
                    // print when the password is not the one registered in the database
                    System.out.println("Invalid password");
                    model.addAttribute("error", "Invalid password");
                }
            } else {
                System.out.println("User/Email not found");
                model.addAttribute("error", "User/email not found");
            }
        } catch (Exception e) {
            e.printStackTrace(); // catch to print if there is an erros
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        }

        return "login"; // If the logIn is not successful, Shows the log in page again 
    }
    
////I modified this to add the part of the rate to this controller, for the petsitter availables to rate!!!!!!1 ERA  @GetMapping("/owner/profile")
    @GetMapping("/owner")
    public String ownerProfile(Model model, HttpSession session) {
        // Get the logged-in user
        User user = (User) session.getAttribute("user");

        // Verify if the user is logged in and if it's an owner
        if (user != null && "OWNER".equalsIgnoreCase(user.getAccount().toString())) {
            model.addAttribute("user", user);

            // Get Pet Sitters related to the Owner
            List<Object[]> sitters = serviceRepository.findSittersByOwnerId(user.getId());
            if (sitters == null || sitters.isEmpty()) {
                System.out.println("No Pet Sitters found for user ID: " + user.getId());
                model.addAttribute("completedSitters", null); // No sitters
            } else {
                System.out.println("Pet Sitters found:");
                for (Object[] sitter : sitters) {
                    System.out.println("Sitter ID: " + sitter[0] + ", Username: " + sitter[1]);
                }
                model.addAttribute("completedSitters", sitters); // Pass sitters to the model
            }

               
         // Check for a success message and pass it to the model
            String success = (String) session.getAttribute("successMessage");
            if (success != null) {
                model.addAttribute("successMessage", success);
                session.removeAttribute("successMessage"); // Limpiar después de mostrar
                System.out.println("Success message added to model and cleared from session");
            }
            
            return "owner"; // Send to owner.html with the information related
        } else {
            return "redirect:/login"; // If not logged in, redirect to login
        }
    }
   
    
    //keep
    @GetMapping("/owner/data")
    public ResponseEntity<Map<String, Object>> getOwnerProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");

        // verify if there is login and if its an owner
        if (user != null && "OWNER".equalsIgnoreCase(user.getAccount().name())) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("fullname", user.getFullname());
            userData.put("birthdate", user.getBirthdate());
            userData.put("email", user.getEmail());
            userData.put("phone", user.getPhone());
            userData.put("address", user.getAddress());
            userData.put("username", user.getUsername());
            userData.put("profilePictureUrl", user.getProfilePictureUrl()); 

            return ResponseEntity.ok(userData);
       } else if (user == null) {
        // messaje to know the problem when i fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "No user found in session. Please log in."));
    } else {
        // error for no valid accounts
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "You are not authorized to view this data."));
    }
} 
       
    
/////////new for the updateOwner page
    
    @PostMapping("/updateOwner")
    public ResponseEntity<String> updateOwnerProfile(@RequestBody Map<String, String> updateData, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null && "OWNER".equalsIgnoreCase(user.getAccount().name())) {
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
    
    
    
    @GetMapping("/updateOwner/data")
    public ResponseEntity<Map<String, Object>> getOwnerProfileData(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null && "OWNER".equalsIgnoreCase(user.getAccount().name())) {
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
    