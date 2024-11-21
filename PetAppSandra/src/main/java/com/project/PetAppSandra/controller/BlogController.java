package com.project.PetAppSandra.controller;

import com.project.PetAppSandra.BlogArticle;
import com.project.PetAppSandra.repository.BlogArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/blog-section")
public class BlogController {

    @Autowired
    private BlogArticleRepository blogArticleRepository;

    @GetMapping
    public String getBlogPage(@RequestParam(name = "week", required = false) Integer week, Model model) {
        List<BlogArticle> articles;
        if (week != null) {
            articles = blogArticleRepository.findByWeek(week); // to give the information according to the week
        } else {
            articles = blogArticleRepository.findAll(); // gets the articles
        }
        model.addAttribute("articles", articles); // it adds the articles
        return "blog"; // shows the information in the page blog.html
    }
}
