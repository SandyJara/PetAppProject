package com.project.PetAppSandra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.PetAppSandra.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

	@GetMapping("/chat")
    public String chatPage(Model model, HttpSession session) {
        // Get the user that is logged
        User currentUser = (User) session.getAttribute("user");

        // Validate if the user is logged
        if (currentUser == null) {
            return "redirect:/login";
        }

        // add username from the user to the model (for Thymeleaf)
        model.addAttribute("currentUser", currentUser.getUsername());
        return "chat"; // file HTML name for Thymeleaf
    }
}