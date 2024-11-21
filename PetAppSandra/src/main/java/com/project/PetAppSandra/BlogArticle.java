package com.project.PetAppSandra;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blog_articles")
public class BlogArticle {
    
    private String title; 
    private String content; // all the information to read
    private int week; // between the number 1 to 1

    // Getters y Setters
   

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getWeek() { return week; }
    public void setWeek(int week) { this.week = week; }
}
