package com.project.PetAppSandra;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "experience")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sitter_id", nullable = false)
    private Long sitterId;

    @Column(name = "experience_text")
    private String experienceText;

    @Column(name = "photo_url")
    private String photoUrl; //leaving this for later, I wont include this now

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;//leaving this for later, I wont include this now

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSitterId() {
        return sitterId;
    }

    public void setSitterId(Long sitterId) {
        this.sitterId = sitterId;
    }

    public String getExperienceText() {
        return experienceText;
    }

    public void setExperienceText(String experienceText) {
        this.experienceText = experienceText;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}