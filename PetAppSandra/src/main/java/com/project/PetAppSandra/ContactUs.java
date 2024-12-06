package com.project.PetAppSandra;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "contact_Us")
public class ContactUs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // Relation table `users`
    private User user;
    @Column(name = "submission_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // I was missing a part of this code and making imposible to update mi table
    private LocalDateTime submissionDate;
    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
}
