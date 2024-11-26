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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        //List<Object[]> results = serviceRepository.findServicesWithPetNameByOwnerId(user.getId());
        // Obtener servicios con el nombre de la mascota y el username del Pet Sitter
       // List<Object[]> results = serviceRepository.findServicesWithSitterUsernameByOwnerId(user.getId());
        List<Object[]> results = serviceRepository.findServicesWithPetAndSitterUsernameByOwnerId(user.getId());
        
        for (Object[] result : results) {
            for (Object obj : result) {
                System.out.print(obj + " ");
            }
            System.out.println();
        }
        
        for (Object[] result : results) {
            System.out.print("sitterUsername: " + result[3]);
            System.out.println(Arrays.toString(result));
        }
        //System.out.println(results);
        
        // Map results to a format to present the information 
        List<Map<String, Object>> services = results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", result[0]); 
            map.put("serviceType", result[1]); 
            map.put("petName", result[2] != null ? result[2] : "N/A"); 
            map.put("sitterUsername", (result[3] != null && !result[3].toString().isEmpty()) ? result[3].toString() : "Unassigned");
            map.put("startDate", result[4]); 
            map.put("endDate", result[5]); 
            map.put("status", result[6]); 
            map.put("description", result[7]); 
            map.put("payment", result[8]); 
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


		
		
///////////////////////AREA FOR THE SEARCH OF SERVICES TO APPLY

//endpoint for the services
		@GetMapping("/pending")
	    public ResponseEntity<?> getPendingServices(
	        @RequestParam(required = false) String serviceType) {
	        
	        // Available services
	        List<Object[]> services = serviceRepository.findPendingServicesWithOwnerAndPet(serviceType);

	        List<Map<String, Object>> response = services.stream().map(service -> {
	            Map<String, Object> map = new HashMap<>();
	            map.put("id", service[0]);
	            map.put("serviceType", service[1]);
	            map.put("petName", service[2]);
	            map.put("ownerName", service[3]);
	            map.put("startDate", service[4]);
	            map.put("endDate", service[5]);
	            map.put("payment", service[6]); 
	            map.put("description", service[7]);
	            return map;
	        }).collect(Collectors.toList());

	        return ResponseEntity.ok(response);
	    }
	
		
		
//endpoint to apply for a service
		@PostMapping("/api/services/{serviceId}/apply")
		public ResponseEntity<?> applyToService(@PathVariable Long serviceId, HttpSession session) {
		    User user = (User) session.getAttribute("user");

		    if (user == null || !"PETSITTER".equalsIgnoreCase(user.getAccount().name())) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only Pet Sitters can apply to this.");
		    }

		    Optional<Service> optionalService = serviceRepository.findById(serviceId);
		    if (optionalService.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
		    }

		    Service service = optionalService.get();
		    if (!"PENDING".equalsIgnoreCase(service.getStatus())) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Service is not available.");
		    }

		    service.setSitterId(user.getId());
		    service.setStatus("APPLIED");
		    serviceRepository.save(service);

		    return ResponseEntity.ok("You have successfully applied to the service.");
		}
		
		
		@PostMapping("/api/services/{serviceId}/accept")
		public ResponseEntity<?> acceptSitter(@PathVariable Long serviceId, HttpSession session) {
		    User owner = (User) session.getAttribute("user");

		    if (owner == null || !"OWNER".equalsIgnoreCase(owner.getAccount().name())) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only Owners can accept applications.");
		    }

		    Optional<Service> serviceOpt = serviceRepository.findById(serviceId);
		    if (serviceOpt.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
		    }

		    Service service = serviceOpt.get();

		    // Verificar que el servicio esté en estado APPLIED y que pertenezca al Owner
		    if (!"APPLIED".equalsIgnoreCase(service.getStatus()) || !service.getOwnerId().equals(owner.getId())) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot accept this service.");
		    }

		    // Cambiar el estado a ACCEPTED
		    service.setStatus("ACCEPTED");
		    serviceRepository.save(service);

		    return ResponseEntity.ok("You have successfully accepted the Pet Sitter.");
		}
		
		
		
		
		
		
//to obtain detail of the service from an owner and join it with the pet sitter
		@GetMapping("/api/services/{serviceId}/details")
		public ResponseEntity<?> getServiceDetails(@PathVariable Long serviceId) {
		    Optional<Object[]> result = serviceRepository.findServiceWithSitterName(serviceId);

		    if (result.isPresent()) {
		        return ResponseEntity.ok(result.get());
		    } else {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
		    }
		}
		
//to change status when a owner ACCEPTS A petsitter
		@PostMapping("/{id}/accept")
	    public ResponseEntity<?> acceptService(@PathVariable Long id) {
	        Optional<Service> serviceOpt = serviceRepository.findById(id);
	        if (serviceOpt.isPresent()) {
	            Service service = serviceOpt.get();
	            if (service.getStatus().equals("APPLIED")) { // Ensure only 'APPLIED' can be accepted
	                service.setStatus("ACCEPTED");
	                serviceRepository.save(service);
	                return ResponseEntity.ok("Service accepted successfully.");
	            } else {
	                return ResponseEntity.badRequest().body("Service is not in APPLIED status.");
	            }
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
	    }

		
//to change status when a owner REJECTS A petsitter		
		@PostMapping("/{id}/reject")
	    public ResponseEntity<?> rejectService(@PathVariable Long id) {
	        Optional<Service> serviceOpt = serviceRepository.findById(id);
	        if (serviceOpt.isPresent()) {
	            Service service = serviceOpt.get();
	            if (service.getStatus().equals("APPLIED")) { // Ensure only 'APPLIED' can be rejected
	                service.setStatus("PENDING"); // Revert to 'PENDING' when rejected
	                service.setSitterId(null); // Remove the assigned pet sitter
	                serviceRepository.save(service); // Save changes to the database
	                serviceRepository.save(service);
	                return ResponseEntity.ok("Service rejected successfully.");
	            } else {
	                return ResponseEntity.badRequest().body("Service is not in APPLIED status.");
	            }
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
	    }

		
		
}
