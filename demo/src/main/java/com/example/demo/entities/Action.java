package com.example.demo.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String description;
    private String location;
    private Double goalAmount;
    private Double currentAmount = 0.0;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;
    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
    private List<Donation> donations;

    public Action() {
    }

    public Action(Integer id, String title, String description, String location, Double goalAmount, Organisation organisation, List<Donation> donations) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.goalAmount = goalAmount;
        this.organisation = organisation;
        this.donations = donations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(Double goalAmount) {
        this.goalAmount = goalAmount;
    }
     public Double getCurrentAmount() { 
        return currentAmount; 
    }
    public void setCurrentAmount(Double currentAmount) { 
        this.currentAmount = currentAmount; 
    }
 
    public String getCategory() {
         return category;
         }
    public void setCategory(String category) { 
        this.category = category; 
    }
 
    public LocalDate getStartDate() {
         return startDate;
         }
    public void setStartDate(LocalDate startDate) {
         this.startDate = startDate; 
        }
 
    public LocalDate getEndDate() { 
        return endDate;
     }
    public void setEndDate(LocalDate endDate) { 
        this.endDate = endDate;
     }
 
    public String getImageUrl() { 
        return imageUrl;
     }
    public void setImageUrl(String imageUrl) {
         this.imageUrl = imageUrl;
         }
 
    public String getStatus() {
         return status; 
        }
    public void setStatus(String status) { 
        this.status = status;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
    public Double getProgressPercentage() {
        if (goalAmount == null || goalAmount == 0) return 0.0;
        return Math.min((currentAmount / goalAmount) * 100, 100.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;
        Action action = (Action) o;
        return Objects.equals(id, action.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
