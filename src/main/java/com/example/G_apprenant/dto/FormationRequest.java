package com.example.G_apprenant.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class FormationRequest {
    @NotBlank(message = "Le nom de la formation est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String nom;

    private String description;

    @NotNull(message = "La durée est obligatoire")
    @Positive(message = "La durée doit être positive")
    private Integer duree;

    @NotNull(message = "Les frais sont obligatoires")
    @DecimalMin(value = "0.0", inclusive = false, message = "Les frais doivent être positifs")
    private BigDecimal frais;

    // Constructeurs
    public FormationRequest() {}

    public FormationRequest(String nom, String description, Integer duree, BigDecimal frais) {
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.frais = frais;
    }

    // Getters and Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public BigDecimal getFrais() {
        return frais;
    }

    public void setFrais(BigDecimal frais) {
        this.frais = frais;
    }
}
