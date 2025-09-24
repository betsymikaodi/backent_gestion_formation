package com.example.G_apprenant.service.impl;

import com.example.G_apprenant.entity.Utilisateur;
import com.example.G_apprenant.repository.UtilisateurRepository;
import com.example.G_apprenant.service.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurServiceImpl.class);
    
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, 
                                PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CRUD de base
    @Override
    public Utilisateur create(Utilisateur utilisateur) {
        logger.info("Création d'un nouvel utilisateur : {}", utilisateur.getEmail());
        
        // Vérifier si l'email existe déjà
        if (existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        
        // Hasher le mot de passe
        if (utilisateur.getMotDePasseHash() != null) {
            utilisateur.setMotDePasseHash(hashPassword(utilisateur.getMotDePasseHash()));
        }
        
        // Initialiser les champs par défaut
        utilisateur.setStatut(Utilisateur.Statut.ACTIF);
        utilisateur.setEmailVerifie(false);
        utilisateur.setCompteVerrouille(false);
        utilisateur.setTentativesConnexionEchec(0);
        
        Utilisateur savedUser = utilisateurRepository.save(utilisateur);
        logger.info("Utilisateur créé avec succès avec l'ID : {}", savedUser.getIdUtilisateur());
        
        return savedUser;
    }

    @Override
    public Utilisateur update(Long id, Utilisateur utilisateur) {
        logger.info("Mise à jour de l'utilisateur ID : {}", id);
        
        Utilisateur existingUser = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        
        // Vérifier si le nouvel email n'est pas déjà utilisé par un autre utilisateur
        if (!existingUser.getEmail().equals(utilisateur.getEmail()) && 
            existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        
        // Mettre à jour les champs autorisés
        existingUser.setEmail(utilisateur.getEmail());
        existingUser.setPrenom(utilisateur.getPrenom());
        existingUser.setNom(utilisateur.getNom());
        existingUser.setTelephone(utilisateur.getTelephone());
        existingUser.setRole(utilisateur.getRole());
        existingUser.setStatut(utilisateur.getStatut());
        existingUser.setPreferencesLangue(utilisateur.getPreferencesLangue());
        existingUser.setThemePreference(utilisateur.getThemePreference());
        
        // Ne pas mettre à jour le mot de passe ici, utiliser changePassword à la place
        
        return utilisateurRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        logger.info("Suppression de l'utilisateur ID : {}", id);
        
        if (!utilisateurRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
        
        utilisateurRepository.deleteById(id);
        logger.info("Utilisateur supprimé avec succès");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Utilisateur> getAll(Pageable pageable) {
        return utilisateurRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Utilisateur> search(Specification<Utilisateur> spec, Pageable pageable) {
        return utilisateurRepository.findAll(spec, pageable);
    }

    // Recherches spécifiques
    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findActiveUserByEmail(String email) {
        return utilisateurRepository.findActiveUserByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> findByRole(Utilisateur.Role role) {
        return utilisateurRepository.findByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> findByStatut(Utilisateur.Statut statut) {
        return utilisateurRepository.findByStatut(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> searchUsers(String terme) {
        return utilisateurRepository.rechercherUtilisateurs(terme);
    }

    // Authentification et sécurité
    @Override
    public Optional<Utilisateur> authenticate(String email, String motDePasse) {
        logger.info("Tentative d'authentification pour l'email : {}", email);
        
        Optional<Utilisateur> userOpt = findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.warn("Aucun utilisateur trouvé avec l'email : {}", email);
            return Optional.empty();
        }
        
        Utilisateur user = userOpt.get();
        
        // Vérifications simplifiées pour les tests
        if (!user.getStatut().equals(Utilisateur.Statut.ACTIF)) {
            logger.warn("Compte inactif pour l'email : {}", email);
            return Optional.empty();
        }
        
        if (user.getCompteVerrouille() != null && user.getCompteVerrouille()) {
            logger.warn("Compte verrouillé pour l'email : {}", email);
            return Optional.empty();
        }
        
        // Vérifier le mot de passe
        if (!verifyPassword(motDePasse, user.getMotDePasseHash())) {
            logger.warn("Mot de passe incorrect pour l'email : {}", email);
            return Optional.empty();
        }
        
        // Connexion réussie - version simplifiée
        logger.info("Authentification réussie pour l'email : {}", email);
        return Optional.of(user);
    }

    @Override
    public void resetFailedAttempts(Long userId) {
        utilisateurRepository.updateSuccessfulLogin(userId, LocalDateTime.now());
    }

    @Override
    public void incrementFailedAttempts(String email) {
        utilisateurRepository.incrementFailedLoginAttempts(email);
    }

    @Override
    public void lockAccount(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        user.setCompteVerrouille(true);
        utilisateurRepository.save(user);
        logger.info("Compte verrouillé pour l'utilisateur ID : {}", userId);
    }

    @Override
    public void unlockAccount(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        user.setCompteVerrouille(false);
        user.setTentativesConnexionEchec(0);
        utilisateurRepository.save(user);
        logger.info("Compte déverrouillé pour l'utilisateur ID : {}", userId);
    }

    // Gestion des mots de passe
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        logger.info("Changement de mot de passe pour l'utilisateur ID : {}", userId);
        
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        // Vérifier l'ancien mot de passe
        if (!verifyPassword(oldPassword, user.getMotDePasseHash())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }
        
        // Mettre à jour avec le nouveau mot de passe
        user.setMotDePasseHash(hashPassword(newPassword));
        utilisateurRepository.save(user);
        
        logger.info("Mot de passe changé avec succès pour l'utilisateur ID : {}", userId);
    }

    @Override
    public void resetPasswordRequest(String email) {
        logger.info("Demande de réinitialisation de mot de passe pour l'email : {}", email);
        
        Optional<Utilisateur> userOpt = findByEmail(email);
        if (userOpt.isEmpty()) {
            // Ne pas révéler si l'email existe ou non pour des raisons de sécurité
            logger.warn("Demande de réinitialisation pour un email inexistant : {}", email);
            return;
        }
        
        // Générer un token de réinitialisation
        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1); // Expire dans 1 heure
        
        utilisateurRepository.setPasswordResetToken(email, token, expiration);
        
        // TODO: Envoyer l'email avec le token (implémentation du service d'email nécessaire)
        logger.info("Token de réinitialisation généré pour l'email : {}", email);
    }

    @Override
    public void resetPasswordWithToken(String token, String newPassword) {
        logger.info("Réinitialisation de mot de passe avec token");
        
        if (!validateResetToken(token)) {
            throw new IllegalArgumentException("Token de réinitialisation invalide ou expiré");
        }
        
        utilisateurRepository.resetPasswordWithToken(token, hashPassword(newPassword));
        logger.info("Mot de passe réinitialisé avec succès");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateResetToken(String token) {
        Optional<Utilisateur> userOpt = utilisateurRepository
                .findByTokenReinitialisationAndTokenExpirationAfter(token, LocalDateTime.now());
        return userOpt.isPresent();
    }

    // Validation et vérification
    @Override
    public void verifyEmail(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        user.setEmailVerifie(true);
        utilisateurRepository.save(user);
        logger.info("Email vérifié pour l'utilisateur ID : {}", userId);
    }

    @Override
    public void sendEmailVerification(Long userId) {
        // TODO: Implémenter l'envoi d'email de vérification
        logger.info("Envoi d'email de vérification pour l'utilisateur ID : {}", userId);
    }

    // Statistiques et rapports
    @Override
    @Transactional(readOnly = true)
    public Long countActiveUsers() {
        return utilisateurRepository.countActiveUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countUsersByRole(Utilisateur.Role role) {
        return utilisateurRepository.countActiveUsersByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countNewUsersSince(LocalDateTime date) {
        return utilisateurRepository.countNewUsersSince(date);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countRecentLoginsSince(LocalDateTime date) {
        return utilisateurRepository.countRecentLoginsSince(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> findActiveUsersSince(LocalDateTime since) {
        return utilisateurRepository.findActiveUsersSince(since);
    }

    // Gestion du profil
    @Override
    public Utilisateur updateProfile(Long userId, String prenom, String nom, String telephone) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        user.setPrenom(prenom);
        user.setNom(nom);
        user.setTelephone(telephone);
        
        return utilisateurRepository.save(user);
    }

    @Override
    public Utilisateur updatePreferences(Long userId, String langue, Utilisateur.Theme theme) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        user.setPreferencesLangue(langue);
        user.setThemePreference(theme);
        
        return utilisateurRepository.save(user);
    }

    // Méthodes utilitaires
    @Override
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    @Override
    public void updateLastLogin(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        user.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(user);
    }
}