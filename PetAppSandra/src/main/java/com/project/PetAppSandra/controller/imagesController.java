package com.project.PetAppSandra.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class imagesController {

    private Storage storage;
    
    @Autowired
    private UserRepository userRepository; // to interact with the database
    

    public imagesController() {
        try {
            String jsonPath = "src/main/resources/petxie.json";
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");
            this.storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping("/upload/profile/{userId}")
    public ResponseEntity<?> uploadProfilePicture(
    		@PathVariable Long userId,
            @RequestParam("file") MultipartFile file,
            HttpSession session) { // add HttpSession as a parameter
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("the file is empty");
            }
            String fileName = "profile_pictures/" + userId + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of("petxie_project", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getBytes());

            String fileUrl = "https://storage.googleapis.com/petxie_project/" + fileName;
            
            
            // New code to update the database with the URL
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setProfilePictureUrl(fileUrl); 
                userRepository.save(user); 
                
             // update session with user 
                session.setAttribute("user", user);
                
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
            }

            // answer then it works
            return ResponseEntity.ok(Map.of("message", "Photo uploaded successfully", "url", fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading the file: " + e.getMessage());
        }
    }
}
    
   