package com.example.G_apprenant.dto;

import jakarta.validation.constraints.*;
import com.example.G_apprenant.entity.Utilisateur;

public class UtilisateurRequest {
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, max = 50, message = "Le mot de passe doit contenir entre 6 et 50 caractères")
    private String motDePasse;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String nom;

    private Utilisateur.Role role = Utilisateur.Role.USER;

    @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
    private String telephone;

    @Size(max = 5, message = "La préférence de langue ne peut pas dépasser 5 caractères")
    private String preferencesLangue = "fr";

    private Utilisateur.Theme themePreference = Utilisateur.Theme.LIGHT;

    // Constructeurs
    public UtilisateurRequest() {}

    public UtilisateurRequest(String email, String motDePasse, String prenom, String nom) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.prenom = prenom;
        this.nom = nom;
    }

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Utilisateur.Role getRole() {
        return role;
    }

    public void setRole(Utilisateur.Role role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPreferencesLangue() {
        return preferencesLangue;
    }

    public void setPreferencesLangue(String preferencesLangue) {
        this.preferencesLangue = preferencesLangue;
    }

    public Utilisateur.Theme getThemePreference() {
        return themePreference;
    }

    public void setThemePreference(Utilisateur.Theme themePreference) {
        this.themePreference = themePreference;
    }

    @Override
    public String toString() {
        return "UtilisateurRequest{" +
                "email='" + email + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", role=" + role +
                '}';
    }
}
