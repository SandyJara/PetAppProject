package com.project.PetAppSandra;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class GoogleCloudStorageConfig {

    private static Storage storage;

    // Method to start storage 
    public static Storage initializeStorage() {
        if (storage == null) {
            try {
                System.out.println("Saving credentials: src/main/resources/petxie.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new FileInputStream("src/main/resources/petxie.json"))
                        .createScoped("https://www.googleapis.com/auth/cloud-platform");
                storage = StorageOptions.newBuilder()
                        .setCredentials(credentials)
                        .build()
                        .getService();
                System.out.println("Client for storage cofigurated correct.");
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Not possible to start Google Cloud Storage", e);
            }
        }
        return storage;
    }

    // To list the files in the bucket i created 
    public static void listFiles(String bucketName) {
        try {
            System.out.println("making list in the bucket: " + bucketName);
            Iterable<Blob> blobs = initializeStorage().list(bucketName).iterateAll();
            if (!blobs.iterator().hasNext()) {
                System.out.println("No files found");
            } else {
                System.out.println("Files found:");
                for (Blob blob : blobs) {
                    System.out.println("File name: " + blob.getName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
