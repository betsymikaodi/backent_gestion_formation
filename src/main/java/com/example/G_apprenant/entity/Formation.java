package com.example.G_apprenant.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "formation", indexes = {
        @Index(name = "idx_formation_nom", columnList = "nom")
})
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formation")
    private Long idFormation;

    @NotBlank(message = "Le nom de la formation est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "La durée est obligatoire")
    @Positive(message = "La durée doit être positive")
    @Column(nullable = false)
    private Integer duree; // en heures

    @NotNull(message = "Les frais sont obligatoires")
    @DecimalMin(value = "0.0", inclusive = false, message = "Les frais doivent être positifs")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal frais;

    @CreationTimestamp
    @Column(name = "date_now", nullable = false, updatable = false)
    private LocalDateTime dateNow;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Inscription> inscriptions = new HashSet<>();

    public Formation() {}

    public Formation(String nom, String description, Integer duree, BigDecimal frais) {
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.frais = frais;
    }

    // Getters and Setters
    public Long getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(Long idFormation) {
        this.idFormation = idFormation;
    }

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

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public LocalDateTime getDateNow() {
        return dateNow;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    // Méthodes utilitaires
    public void addInscription(Inscription inscription) {
        inscriptions.add(inscription);
        inscription.setFormation(this);
    }

    public void removeInscription(Inscription inscription) {
        inscriptions.remove(inscription);
        inscription.setFormation(null);
    }

    public int getNombreInscrits() {
        return inscriptions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Formation)) return false;
        Formation formation = (Formation) o;
        return idFormation != null && idFormation.equals(formation.idFormation);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idFormation=" + idFormation +
                ", nom='" + nom + '\'' +
                ", duree=" + duree +
                ", frais=" + frais +
                '}';
    }
}
