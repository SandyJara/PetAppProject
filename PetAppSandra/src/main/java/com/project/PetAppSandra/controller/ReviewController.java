package com.project.PetAppSandra.controller;

import com.project.PetAppSandra.Review;
import com.project.PetAppSandra.User;
import com.project.PetAppSandra.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/owner/rate-sitter")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public String ratePetSitter(
            @RequestParam Long sitterId, 
            @RequestParam int rating,
            @RequestParam String comment,
            HttpSession session) {

        System.out.println("Entering ratePetSitter");

        // get the user that is logged
        User user = (User) session.getAttribute("user");
        if (user == null || !"OWNER".equalsIgnoreCase(user.getAccount().toString())) {
            System.out.println("No user found in session or not an owner. Redirecting to login.");
            return "redirect:/login";
        }

        Long ownerId = user.getId();
        System.out.println("Owner ID: " + ownerId);

        // create and save review
        Review review = new Review();
        review.setOwnerId(ownerId);
        review.setSitterId(sitterId);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());

        reviewRepository.save(review);
        System.out.println("Review saved successfully.");

        //return "redirect:/owner?success=rating_submitted"; // go to Owner page updated with the successful message
        return "redirect:/owner?success=true";
    }
}
