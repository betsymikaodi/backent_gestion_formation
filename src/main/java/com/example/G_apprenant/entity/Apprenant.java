package com.example.G_apprenant.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "apprenant", indexes = {
        @Index(name = "idx_apprenant_email", columnList = "email"),
        @Index(name = "idx_apprenant_cin", columnList = "cin")
})
public class Apprenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apprenant")
    private Long idApprenant;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    @Column(nullable = false, length = 50)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
    @Column(nullable = false, length = 50)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
    @Column(length = 20)
    private String telephone;

    @Column(columnDefinition = "TEXT")
    private String adresse;

    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @NotBlank(message = "Le CIN est obligatoire")
    @Size(max = 20, message = "Le CIN ne peut pas dépasser 20 caractères")
    @Column(nullable = false, unique = true, length = 20)
    private String cin;

    @CreationTimestamp
    @Column(name = "date_now", nullable = false, updatable = false)
    private java.time.LocalDateTime dateNow;

    @OneToMany(mappedBy = "apprenant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Inscription> inscriptions = new HashSet<>();

    public Apprenant() {}

    public Apprenant(String nom, String prenom, String email, String cin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.cin = cin;
    }

    // Getters and Setters
    public Long getIdApprenant() {
        return idApprenant;
    }

    public void setIdApprenant(Long idApprenant) {
        this.idApprenant = idApprenant;
    }

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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public java.time.LocalDateTime getDateNow() {
        return dateNow;
    }

    // Méthodes utilitaires
    public void addInscription(Inscription inscription) {
        inscriptions.add(inscription);
        inscription.setApprenant(this);
    }

    public void removeInscription(Inscription inscription) {
        inscriptions.remove(inscription);
        inscription.setApprenant(null);
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Apprenant)) return false;
        Apprenant apprenant = (Apprenant) o;
        return idApprenant != null && idApprenant.equals(apprenant.idApprenant);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Apprenant{" +
                "idApprenant=" + idApprenant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", cin='" + cin + '\'' +
                '}';
    }
}
