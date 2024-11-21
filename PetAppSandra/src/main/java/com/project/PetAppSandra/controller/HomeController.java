package com.project.PetAppSandra.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	

	@GetMapping("/home")
	public String home() {
	    return "home"; 
	}

	@GetMapping("/")
	public String index() {
	    return "home"; 
	}

	@GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // to close session --review because it has to work with a button
        return "redirect:/home"; // goes back to home
    }
	
	
	@GetMapping("/aboutUs")
	public String aboutUs() {
	    return "aboutUs";
	}

	@GetMapping("/blog")
	public String blog() {
	    return "blog"; 
	}

	@GetMapping("/contact")
	public String contact() {
	    return "contact"; 
	}

	@GetMapping("/owner")
	public String ownerPage() {
	    return "owner"; 
	}

	@GetMapping("/petSitter")
	public String petSitterPage() {
	    return "petSitter"; 
	}

	@GetMapping("/updateOwner")
	public String updateOwnerPage() {
	    return "updateOwner"; 
	}
	
	@GetMapping("/updatePetSitter")
	public String updatePetSitterPage() {
	    return "updatePetSitter"; 
	}
	
	}

