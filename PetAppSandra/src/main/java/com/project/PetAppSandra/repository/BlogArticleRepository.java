package com.project.PetAppSandra.repository;


import com.project.PetAppSandra.BlogArticle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.util.List;


public interface BlogArticleRepository extends MongoRepository<BlogArticle, String> {
    List<BlogArticle> findByWeek(int week);
}