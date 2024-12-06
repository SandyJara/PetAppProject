package com.project.PetAppSandra.controller;
import com.project.PetAppSandra.ContactUs;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.ContactMessageRepository;
import com.project.PetAppSandra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactMessageRepository contactMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody ContactUs contactMessage) {
        System.out.println("Received message: " + contactMessage.getMessage());
        System.out.println("Received email: " + contactMessage.getEmail());
        // Check if email exists in users table
        Optional<User> userOptional = userRepository.findByEmail(contactMessage.getEmail());
        userOptional.ifPresent(contactMessage::setUser);
        System.out.println("Saving the contact message...");
        try {
            contactMessageRepository.save(contactMessage);
            System.out.println("Message saved successfully!");
        } catch (Exception e) {
            System.out.println("Error while saving the message: " + e.getMessage());
            return ResponseEntity.status(500).body("Error while saving the message.");
        }
        return ResponseEntity.ok("Message sent successfully");
    }
}