package com.example.G_apprenant.dto;

import java.time.LocalDate;
import org.springframework.data.domain.Sort;

public class ApprenantSearchCriteria {
    // Critères de recherche
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String cin;
    private LocalDate dateNaissanceDebut;
    private LocalDate dateNaissanceFin;
    private String statutInscription;
    private Long formationId;
    private String nomFormation;
    
    // Paramètres de pagination
    private int page = 0;
    private int size = 20; // Taille par défaut
    
    // Paramètres de tri
    private String sortBy = "dateNow";
    private Sort.Direction sortDirection = Sort.Direction.DESC;
    
    // Recherche globale
    private String searchTerm; // Recherche dans nom, prenom, email
    
    public ApprenantSearchCriteria() {}
    
    // Getters and Setters
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getCin() {
        return cin;
    }
    
    public void setCin(String cin) {
        this.cin = cin;
    }
    
    public LocalDate getDateNaissanceDebut() {
        return dateNaissanceDebut;
    }
    
    public void setDateNaissanceDebut(LocalDate dateNaissanceDebut) {
        this.dateNaissanceDebut = dateNaissanceDebut;
    }
    
    public LocalDate getDateNaissanceFin() {
        return dateNaissanceFin;
    }
    
    public void setDateNaissanceFin(LocalDate dateNaissanceFin) {
        this.dateNaissanceFin = dateNaissanceFin;
    }
    
    public String getStatutInscription() {
        return statutInscription;
    }
    
    public void setStatutInscription(String statutInscription) {
        this.statutInscription = statutInscription;
    }
    
    public Long getFormationId() {
        return formationId;
    }
    
    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }
    
    public String getNomFormation() {
        return nomFormation;
    }
    
    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = Math.max(0, page);
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        // Limiter les tailles autorisées pour éviter la surcharge
        if (size <= 0) {
            this.size = 20;
        } else if (size > 100) {
            this.size = 100;
        } else {
            this.size = size;
        }
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        // Valider les champs de tri autorisés
        if (sortBy != null && isValidSortField(sortBy)) {
            this.sortBy = sortBy;
        } else {
            this.sortBy = "dateNow";
        }
    }
    
    public Sort.Direction getSortDirection() {
        return sortDirection;
    }
    
    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection != null ? sortDirection : Sort.Direction.DESC;
    }
    
    public void setSortDirection(String sortDirection) {
        if ("asc".equalsIgnoreCase(sortDirection)) {
            this.sortDirection = Sort.Direction.ASC;
        } else if ("desc".equalsIgnoreCase(sortDirection)) {
            this.sortDirection = Sort.Direction.DESC;
        }
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    // Méthodes utilitaires
    private boolean isValidSortField(String field) {
        return switch (field.toLowerCase()) {
            case "nom", "prenom", "email", "telephone", "dateNaissance", "cin", "dateNow" -> true;
            default -> false;
        };
    }
    
    public boolean hasSearchCriteria() {
        return nom != null || prenom != null || email != null || telephone != null ||
               adresse != null || cin != null || dateNaissanceDebut != null ||
               dateNaissanceFin != null || statutInscription != null ||
               formationId != null || nomFormation != null || searchTerm != null;
    }
    
    public Sort getSort() {
        return Sort.by(sortDirection, sortBy);
    }
}
