package com.project.PetAppSandra;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sitter_id")
    private Long sitterId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "preferred_species")
    private String preferredSpecies;

    @Column(name = "preferred_size")
    private String preferredSize;

    @Column(name = "area")
    private String area;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;
    
    @Column(name = "payment")
    private Integer payment;

    // Getters y Setters
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPreferredSpecies() {
        return preferredSpecies;
    }

    public void setPreferredSpecies(String preferredSpecies) {
        this.preferredSpecies = preferredSpecies;
    }

    public String getPreferredSize() {
        return preferredSize;
    }

    public void setPreferredSize(String preferredSize) {
        this.preferredSize = preferredSize;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}
    
    
}
