package com.project.PetAppSandra.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.project.PetAppSandra.Service;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.ServiceRepository;
import com.project.PetAppSandra.repository.UserRepository;

@RestController
@RequestMapping("/api/maps")
public class GoogleMapsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    private final String GOOGLE_MAPS_API_KEY = "AIzaSyB4KM3yCfY-VigXWWnysSRv906eCWeXmFo";

    @GetMapping("/services")
    public ResponseEntity<List<Map<String, Object>>> getPendingServicesWithLocations() {
        try {
            // Get services with PENDING status
            List<Service> pendingServices = serviceRepository.findByStatus("PENDING");

            if (pendingServices == null || pendingServices.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Create a list to storage services with locations
            List<Map<String, Object>> servicesWithLocations = new ArrayList<>();

            RestTemplate restTemplate = new RestTemplate();

            // Process each service and convert the address with the owner coordinates
            for (Service service : pendingServices) {
                User owner = userRepository.findById(service.getOwnerId()).orElse(null);
                if (owner != null && owner.getAddress() != null) {
                    try {
                        // get a URL to get the coordinates
                        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
                                + URLEncoder.encode(owner.getAddress(), StandardCharsets.UTF_8)
                                + "&key=" + GOOGLE_MAPS_API_KEY;

                        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

                        @SuppressWarnings("unchecked")
                        Map<String, Object> body = (Map<String, Object>) response.getBody();

                        if ("OK".equals(body.get("status"))) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
                            Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
                            Map<String, Object> location = (Map<String, Object>) geometry.get("location");

                            // Add information with the data services and the location
                            servicesWithLocations.add(Map.of(
                                    "name", service.getPetId() + " (" + owner.getUsername() + ")",
                                    "latitude", location.get("lat"),
                                    "longitude", location.get("lng"),
                                    "type", service.getServiceType(),
                                    "description", service.getDescription()
                            ));
                        }
                    } catch (Exception e) {
                        System.err.println("Error converting address to coordinates for service ID " 
                                + service.getId() + ": " + e.getMessage());
                    }
                }
            }

            return ResponseEntity.ok(servicesWithLocations);

        } catch (Exception e) {
            System.err.println("Error processing services: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

 // Method to get the coordinates
    @GetMapping("/getCoordinates")
    public ResponseEntity<?> getCoordinates(@RequestParam String address) {
        try {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + URLEncoder.encode(address, StandardCharsets.UTF_8)
                    + "&key=" + GOOGLE_MAPS_API_KEY;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) response.getBody();

            if ("OK".equals(body.get("status"))) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
                Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
                Map<String, Object> location = (Map<String, Object>) geometry.get("location");

                return ResponseEntity.ok(Map.of(
                        "latitude", location.get("lat"),
                        "longitude", location.get("lng")
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid address or no results found.");
            }
        } catch (Exception e) {
            System.err.println("Error fetching coordinates for address " + address + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching coordinates.");
        }
    }
}
