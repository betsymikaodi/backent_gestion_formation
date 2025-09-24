package com.example.G_apprenant.service;

import com.example.G_apprenant.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    
    // CRUD de base
    Utilisateur create(Utilisateur utilisateur);
    Utilisateur update(Long id, Utilisateur utilisateur);
    void delete(Long id);
    Optional<Utilisateur> findById(Long id);
    List<Utilisateur> getAll();
    Page<Utilisateur> getAll(Pageable pageable);
    Page<Utilisateur> search(Specification<Utilisateur> spec, Pageable pageable);
    
    // Recherches spécifiques
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findActiveUserByEmail(String email);
    boolean existsByEmail(String email);
    List<Utilisateur> findByRole(Utilisateur.Role role);
    List<Utilisateur> findByStatut(Utilisateur.Statut statut);
    List<Utilisateur> searchUsers(String terme);
    
    // Authentification et sécurité
    Optional<Utilisateur> authenticate(String email, String motDePasse);
    void resetFailedAttempts(Long userId);
    void incrementFailedAttempts(String email);
    void lockAccount(Long userId);
    void unlockAccount(Long userId);
    
    // Gestion des mots de passe
    void changePassword(Long userId, String oldPassword, String newPassword);
    void resetPasswordRequest(String email);
    void resetPasswordWithToken(String token, String newPassword);
    boolean validateResetToken(String token);
    
    // Validation et vérification
    void verifyEmail(Long userId);
    void sendEmailVerification(Long userId);
    
    // Statistiques et rapports
    Long countActiveUsers();
    Long countUsersByRole(Utilisateur.Role role);
    Long countNewUsersSince(LocalDateTime date);
    Long countRecentLoginsSince(LocalDateTime date);
    List<Utilisateur> findActiveUsersSince(LocalDateTime since);
    
    // Gestion du profil
    Utilisateur updateProfile(Long userId, String prenom, String nom, String telephone);
    Utilisateur updatePreferences(Long userId, String langue, Utilisateur.Theme theme);
    
    // Méthodes utilitaires
    String hashPassword(String plainPassword);
    boolean verifyPassword(String plainPassword, String hashedPassword);
    void updateLastLogin(Long userId);
}