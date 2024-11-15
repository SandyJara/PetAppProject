package com.project.PetAppSandra.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
		 @GetMapping("/home")
		    public String home() {
		        return "home"; // This returns the home.html template
		    }

		 
		 @GetMapping("/") // This maps the root URL to the same view as /home
		    public String index() {
		        return "home"; // Same view as the /home mapping
		    }
		 
	    
    
	    @GetMapping("/login")
	    public String login() {
	        return "login";  // returns login.html
	    }
	
	        
	    @PostMapping("/register")
	    public String register(String name, String email, String password) {
	    	return "redirect:/";  // Redirects back to home page after registration
	    }
	    
	    @GetMapping("/blog")
	    public String showBlogPage(Model model) {
	        return "blog"; 
	    }
	    
	    @GetMapping("/aboutUs")
	    public String showAboutPage(Model model) {
	        return "aboutUs"; 
	    }
	    
	    @GetMapping("/contact")
	    public String showContactPage(Model model) {
	        return "contact"; 
	    }
	    
	    @GetMapping("/owner")
	    public String showOwnerPage() {
	        return "owner";  //
	    }
	    
	    @GetMapping("/petSitter")
	    public String showPetSitterPage() {
	        return "petSitter";  //
	    }
	    
	    
	}

