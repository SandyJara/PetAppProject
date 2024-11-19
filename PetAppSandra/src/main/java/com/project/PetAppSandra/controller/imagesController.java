package com.project.PetAppSandra.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class imagesController {

    private Storage storage;

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
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("the file is empty");
            }
            String fileName = "profile_pictures/" + userId + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of("petxie_project", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getBytes());

            String fileUrl = "https://storage.googleapis.com/petxie_project/" + fileName;
            return ResponseEntity.ok(Map.of("message", "File uploaded succesfull", "url", fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error to upload the file: " + e.getMessage());
        }
    }
}