package com.project.PetAppSandra.controller;

import com.project.PetAppSandra.Service;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.ServiceRepository;
import com.project.PetAppSandra.repository.PetRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PetRepository petRepository;

	//Get the information from the pets
    @GetMapping("/owner/pets")
    public ResponseEntity<List<com.project.PetAppSandra.Pet>> getPetsForOwner(HttpSession session) {
        User user = (User) session.getAttribute("user");

        // Verify if the user is not loggged or if its not an owner 
        if (user == null || user.getAccount() != User.AccountType.OWNER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //This part is to get the pets saved in a specific owner profile
        List<com.project.PetAppSandra.Pet> pets = petRepository.findByOwnerId(user.getId());
        return ResponseEntity.ok(pets);
    }



 // Create new service 
    @PostMapping
    public ResponseEntity<String> createService(@RequestBody Service service, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        // If the user is a owner
        if (user.getAccount() == User.AccountType.OWNER) {
            service.setOwnerId(user.getId());
            Optional<com.project.PetAppSandra.Pet> pet = petRepository.findById(service.getPetId());
            if (pet.isEmpty() || !pet.get().getOwnerId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid pet for owner");
            }
        }
        // If the user is a Pet Sitter
        else if (user.getAccount() == User.AccountType.PETSITTER) {
            service.setSitterId(user.getId());
            if (service.getPetId() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pet ID should not be provided by a Pet Sitter");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        // Validation of the informarion 
        if (service.getServiceType() == null || 
            !(service.getServiceType().equalsIgnoreCase("WALK") || 
              service.getServiceType().equalsIgnoreCase("VISIT") || 
              service.getServiceType().equalsIgnoreCase("HOUSESITTING"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid service type");//extra because by standar only allows to select this options anyway
        }

        if (service.getStartDate() == null || service.getEndDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start and End dates are required");
        }

        // Field payment
        if (service.getPayment() == null || service.getPayment() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment must be a positive value.");
        }

        // Value to start with, all the services once created
        service.setStatus("PENDING");

        // Save the service in my database
        serviceRepository.save(service);
        return ResponseEntity.ok("Service created successfully");
    }
		
	//i had it simplified before but it wasnt possible to extract the Pet name form the other table	
    @GetMapping("/owner/services")
    public ResponseEntity<List<Map<String, Object>>> getServicesWithPetName(HttpSession session) {
        User user = (User) session.getAttribute("user");

        // Verify if the user is logged or and an owner 
        if (user == null || user.getAccount() != User.AccountType.OWNER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // To get services with the pets name 
        List<Object[]> results = serviceRepository.findServicesWithPetNameByOwnerId(user.getId());

        // Map results to a format to present the information 
        List<Map<String, Object>> services = results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", result[0]); 
            map.put("serviceType", result[1]); 
            map.put("petName", result[2] != null ? result[2] : "N/A"); 
            map.put("startDate", result[3]); 
            map.put("endDate", result[4]); 
            map.put("status", result[5]); 
            map.put("description", result[6]); 
            map.put("payment", result[7]); 
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(services);
    }
		
		// to delete services
		@DeleteMapping("/{id}")
		public ResponseEntity<String> deleteService(@PathVariable Long id, HttpSession session) {
		    try {
		        User user = (User) session.getAttribute("user");

		        if (user == null || user.getAccount() != User.AccountType.OWNER) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		        }

		        Optional<Service> service = serviceRepository.findById(id);
		        if (service.isPresent()) {
		            // Extra filter to verify that the services is from the logged user
		            if (!service.get().getOwnerId().equals(user.getId())) {
		                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete this service.");
		            }

		            serviceRepository.deleteById(id);
		            return ResponseEntity.ok("Service deleted successfully.");
		        } else {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
		        }
		    } catch (Exception e) {
		        // any unexpected exception
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
		    }
		}

		
		
}
