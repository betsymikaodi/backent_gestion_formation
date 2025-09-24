package com.example.G_apprenant.dto;

import com.example.G_apprenant.entity.Utilisateur;

public class LoginResponse {
    
    private String token;
    private String type = "Bearer";
    private Long idUtilisateur;
    private String email;
    private String prenom;
    private String nom;
    private Utilisateur.Role role;
    private Utilisateur.Statut statut;
    private String message;
    private boolean success;

    // Constructeurs
    public LoginResponse() {}

    public LoginResponse(String token, Utilisateur utilisateur) {
        this.token = token;
        this.idUtilisateur = utilisateur.getIdUtilisateur();
        this.email = utilisateur.getEmail();
        this.prenom = utilisateur.getPrenom();
        this.nom = utilisateur.getNom();
        this.role = utilisateur.getRole();
        this.statut = utilisateur.getStatut();
        this.success = true;
    }
    
    // Constructeur pour les erreurs
    public LoginResponse(String token, String role, String nomComplet, String message, boolean success) {
        this.token = token;
        if (role != null) {
            this.role = Utilisateur.Role.valueOf(role);
        }
        if (nomComplet != null) {
            String[] parts = nomComplet.split(" ", 2);
            this.prenom = parts.length > 0 ? parts[0] : "";
            this.nom = parts.length > 1 ? parts[1] : "";
        }
        this.message = message;
        this.success = success;
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Utilisateur.Statut getStatut() {
        return statut;
    }

    public void setStatut(Utilisateur.Statut statut) {
        this.statut = statut;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "idUtilisateur=" + idUtilisateur +
                ", email='" + email + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", role=" + role +
                ", statut=" + statut +
                '}';
    }
}
