package com.example.G_apprenant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs", indexes = {
        @Index(name = "idx_utilisateurs_email", columnList = "email"),
        @Index(name = "idx_utilisateurs_role", columnList = "role"),
        @Index(name = "idx_utilisateurs_statut", columnList = "statut")
})
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Long idUtilisateur;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(name = "mot_de_passe_hash", nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasseHash;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotNull(message = "Le rôle est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    @Column(name = "tentatives_connexion_echec")
    private Integer tentativesConnexionEchec = 0;

    @Column(name = "compte_verrouille")
    private Boolean compteVerrouille = false;

    @Column(name = "token_reinitialisation", length = 255)
    @JsonIgnore
    private String tokenReinitialisation;

    @Column(name = "token_expiration")
    @JsonIgnore
    private LocalDateTime tokenExpiration;

    @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
    @Column(length = 20)
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut = Statut.ACTIF;

    @Column(name = "email_verifie")
    private Boolean emailVerifie = false;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Size(max = 5, message = "La préférence de langue ne peut pas dépasser 5 caractères")
    @Column(name = "preferences_langue", length = 5)
    private String preferencesLangue = "fr";

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_preference", length = 10)
    private Theme themePreference = Theme.LIGHT;

    // Enums
    public enum Role {
        ADMIN, USER
    }

    public enum Statut {
        ACTIF, INACTIF, SUSPENDU
    }

    public enum Theme {
        LIGHT, DARK, SYSTEM
    }

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(String email, String motDePasseHash, String prenom, String nom, Role role) {
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    // Méthodes du cycle de vie JPA
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    // Getters et Setters
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

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public Integer getTentativesConnexionEchec() {
        return tentativesConnexionEchec;
    }

    public void setTentativesConnexionEchec(Integer tentativesConnexionEchec) {
        this.tentativesConnexionEchec = tentativesConnexionEchec;
    }

    public Boolean getCompteVerrouille() {
        return compteVerrouille;
    }

    public void setCompteVerrouille(Boolean compteVerrouille) {
        this.compteVerrouille = compteVerrouille;
    }

    public String getTokenReinitialisation() {
        return tokenReinitialisation;
    }

    public void setTokenReinitialisation(String tokenReinitialisation) {
        this.tokenReinitialisation = tokenReinitialisation;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Boolean getEmailVerifie() {
        return emailVerifie;
    }

    public void setEmailVerifie(Boolean emailVerifie) {
        this.emailVerifie = emailVerifie;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public String getPreferencesLangue() {
        return preferencesLangue;
    }

    public void setPreferencesLangue(String preferencesLangue) {
        this.preferencesLangue = preferencesLangue;
    }

    public Theme getThemePreference() {
        return themePreference;
    }

    public void setThemePreference(Theme themePreference) {
        this.themePreference = themePreference;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public boolean isAccountLocked() {
        return compteVerrouille != null && compteVerrouille;
    }

    public boolean isEmailVerified() {
        return emailVerifie != null && emailVerifie;
    }

    public boolean isTokenValid() {
        return tokenReinitialisation != null && tokenExpiration != null && 
               LocalDateTime.now().isBefore(tokenExpiration);
    }

    public void incrementFailedAttempts() {
        this.tentativesConnexionEchec = (this.tentativesConnexionEchec == null ? 0 : this.tentativesConnexionEchec) + 1;
        if (this.tentativesConnexionEchec >= 5) {
            this.compteVerrouille = true;
        }
    }

    public void resetFailedAttempts() {
        this.tentativesConnexionEchec = 0;
        this.compteVerrouille = false;
        this.derniereConnexion = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        Utilisateur that = (Utilisateur) o;
        return idUtilisateur != null && idUtilisateur.equals(that.idUtilisateur);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", email='" + email + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", role=" + role +
                ", statut=" + statut +
                '}';
    }
}
