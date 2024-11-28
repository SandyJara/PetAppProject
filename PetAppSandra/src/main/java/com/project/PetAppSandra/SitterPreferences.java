package com.project.PetAppSandra;

import com.google.cloud.Timestamp;

import jakarta.persistence.*;


@Entity
	@Table(name = "sitter_preferences") 

	public class SitterPreferences {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "sitter_id", nullable = false)
	    private Long sitterId; 
	    @Column(name = "service_type")
	    private String serviceType; 
	    @Column(name = "pet_type")
	    private String petType; 
	    @Column(name = "status_profile")
	    private String statusProfile; // ACTIVE or INACTIVE
	    @Column(name = "created_at", updatable = false)
	    private Timestamp createdAt;
	    @Column(name = "updated_at")
	    private Timestamp updatedAt;

	    
	    //Getters and setters
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

		public String getServiceType() {
			return serviceType;
		}

		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}

		

		public String getPetType() {
			return petType;
		}

		public void setPetType(String petType) {
			this.petType = petType;
		}

		public String getStatusProfile() {
			return statusProfile;
		}

		public void setStatusProfile(String statusProfile) {
			this.statusProfile = statusProfile;
		}

		public Timestamp getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(Timestamp createdAt) {
			this.createdAt = createdAt;
		}

		public Timestamp getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(Timestamp updatedAt) {
			this.updatedAt = updatedAt;
		}
	    
	   
	    
	
}
