package com.project.PetAppSandra.controller;

import com.project.PetAppSandra.BlogArticle;
import com.project.PetAppSandra.repository.BlogArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.html.HtmlRenderer;


import java.util.List;

@Controller
@RequestMapping("/blog-section")
public class BlogController {

    @Autowired
    private BlogArticleRepository blogArticleRepository;

    @GetMapping
    public String getBlogPage(@RequestParam(name = "week", required = false) Integer week, Model model) {
        List<BlogArticle> articles;

        try {
            if (week != null) {
                // Allows to search articles by week selected
                articles = blogArticleRepository.findByWeek(week);
                if (articles.isEmpty()) {
                    model.addAttribute("message", "No articles found for week " + week + ".");
                }
            } else {
                articles = blogArticleRepository.findAll();
                if (articles.isEmpty()) {
                    model.addAttribute("message", "No articles available at the moment.");
                }
            }
            
            
         // Convert Markdown content to HTML for each article
            MutableDataSet options = new MutableDataSet();
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();

            for (BlogArticle article : articles) {
                String markdown = article.getContent(); 
                String htmlContent = renderer.render(parser.parse(markdown));  //  Markdown to HTML
                article.setContent(htmlContent); 
            } 
            
            
            
        } catch (Exception e) {
            // catch errors
            model.addAttribute("message", "An unexpected error occurred while retrieving articles.");
            articles = List.of(); 
        }

        model.addAttribute("articles", articles); // this part adds the list emppty o with information 
        return "blog"; // goes back to blog.html
    }
}
