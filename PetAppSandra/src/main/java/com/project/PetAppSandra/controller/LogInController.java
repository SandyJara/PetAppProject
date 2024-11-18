package com.project.PetAppSandra.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LogInController {

    @Autowired
    private UserRepository userRepository;

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
                System.out.println("Usuario encontrado: " + user.getUsername());

                if (user.getPassword() != null && user.getPassword().equals(password)) {
                    // if the password is correct
                    System.out.println("Contraseña correcta");

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
    
    @GetMapping("/owner/profile")
    public String ownerProfile(Model model, HttpSession session) {
        // get the user that is login
        User user = (User) session.getAttribute("user");

        // verify if there is login and if its an owner
        if (user != null && "OWNER".equalsIgnoreCase(user.getAccount().toString())) {
            model.addAttribute("user", user);
            return "owner"; // send to owner.html with the information related
        } else {
            return "redirect:/login"; // if no go to login
        }
    }
    
    
    
    
    
    
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

            return ResponseEntity.ok(userData);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

   
    
}