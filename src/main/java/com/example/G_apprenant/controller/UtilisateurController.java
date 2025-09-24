package com.example.G_apprenant.controller;

import com.example.G_apprenant.dto.UtilisateurRequest;
import com.example.G_apprenant.entity.Utilisateur;
import com.example.G_apprenant.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/utilisateurs")
@Tag(name = "User Management", description = "APIs pour la gestion des utilisateurs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class UtilisateurController {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurController.class);
    
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // ================= OPÉRATIONS CRUD =================

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Créer un utilisateur", description = "Création d'un nouvel utilisateur (ADMIN uniquement)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Utilisateur> create(@Valid @RequestBody UtilisateurRequest request) {
        logger.info("Création d'un nouvel utilisateur : {}", request.getEmail());
        
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(request.getEmail());
            utilisateur.setMotDePasseHash(request.getMotDePasse());
            utilisateur.setPrenom(request.getPrenom());
            utilisateur.setNom(request.getNom());
            utilisateur.setTelephone(request.getTelephone());
            utilisateur.setRole(request.getRole());
            utilisateur.setPreferencesLangue(request.getPreferencesLangue());
            utilisateur.setThemePreference(request.getThemePreference());
            
            Utilisateur savedUser = utilisateurService.create(utilisateur);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Erreur de validation : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Liste des utilisateurs", description = "Récupère tous les utilisateurs avec pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Utilisateur>> getAll(
            @Parameter(description = "Numéro de page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Tri par champ") @RequestParam(defaultValue = "dateCreation") String sortBy,
            @Parameter(description = "Direction du tri") @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Utilisateur> users = utilisateurService.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Utilisateur par ID", description = "Récupère un utilisateur par son ID")
    @PreAuthorize("hasRole('ADMIN') or @utilisateurController.isOwnerOrAdmin(#id)")
    public ResponseEntity<Utilisateur> getById(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        
        Optional<Utilisateur> user = utilisateurService.findById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modifier un utilisateur", description = "Met à jour les informations d'un utilisateur")
    @PreAuthorize("hasRole('ADMIN') or @utilisateurController.isOwnerOrAdmin(#id)")
    public ResponseEntity<Utilisateur> update(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id,
            @Valid @RequestBody UtilisateurRequest request) {
        
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(request.getEmail());
            utilisateur.setPrenom(request.getPrenom());
            utilisateur.setNom(request.getNom());
            utilisateur.setTelephone(request.getTelephone());
            utilisateur.setRole(request.getRole());
            utilisateur.setPreferencesLangue(request.getPreferencesLangue());
            utilisateur.setThemePreference(request.getThemePreference());
            
            Utilisateur updatedUser = utilisateurService.update(id, utilisateur);
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur (ADMIN uniquement)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        
        try {
            utilisateurService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ================= RECHERCHE ET FILTRAGE =================

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher des utilisateurs", description = "Recherche des utilisateurs par terme")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Utilisateur>> search(
            @Parameter(description = "Terme de recherche") @RequestParam String terme) {
        
        List<Utilisateur> users = utilisateurService.searchUsers(terme);
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/by-role/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Utilisateurs par rôle", description = "Récupère les utilisateurs par rôle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Utilisateur>> getByRole(
            @Parameter(description = "Rôle") @PathVariable Utilisateur.Role role) {
        
        List<Utilisateur> users = utilisateurService.findByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/by-status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Utilisateurs par statut", description = "Récupère les utilisateurs par statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Utilisateur>> getByStatus(
            @Parameter(description = "Statut") @PathVariable Utilisateur.Statut status) {
        
        List<Utilisateur> users = utilisateurService.findByStatut(status);
        return ResponseEntity.ok(users);
    }

    // ================= GESTION DU PROFIL =================

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Mon profil", description = "Récupère le profil de l'utilisateur connecté")
    public ResponseEntity<Utilisateur> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        Optional<Utilisateur> user = utilisateurService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modifier mon profil", description = "Met à jour le profil de l'utilisateur connecté")
    public ResponseEntity<Utilisateur> updateProfile(@Valid @RequestBody UtilisateurRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        try {
            Optional<Utilisateur> userOpt = utilisateurService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Long userId = userOpt.get().getIdUtilisateur();
            Utilisateur updatedUser = utilisateurService.updateProfile(
                    userId, request.getPrenom(), request.getNom(), request.getTelephone());
            
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du profil : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/profile/preferences", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modifier mes préférences", description = "Met à jour les préférences de l'utilisateur")
    public ResponseEntity<Utilisateur> updatePreferences(@Valid @RequestBody UtilisateurRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        try {
            Optional<Utilisateur> userOpt = utilisateurService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Long userId = userOpt.get().getIdUtilisateur();
            Utilisateur updatedUser = utilisateurService.updatePreferences(
                    userId, request.getPreferencesLangue(), request.getThemePreference());
            
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour des préférences : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ================= GESTION DES MOTS DE PASSE =================

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Changer mot de passe", description = "Change le mot de passe de l'utilisateur connecté")
    public ResponseEntity<Map<String, Object>> changePassword(
            @Parameter(description = "Ancien mot de passe") @RequestParam String oldPassword,
            @Parameter(description = "Nouveau mot de passe") @RequestParam String newPassword) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Utilisateur> userOpt = utilisateurService.findByEmail(email);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Utilisateur non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Long userId = userOpt.get().getIdUtilisateur();
            utilisateurService.changePassword(userId, oldPassword, newPassword);
            
            response.put("success", true);
            response.put("message", "Mot de passe changé avec succès");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Erreur lors du changement de mot de passe : {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= ADMINISTRATION =================

    @PostMapping(value = "/admin/{id}/lock", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verrouiller un compte", description = "Verrouille le compte d'un utilisateur (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> lockAccount(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            utilisateurService.lockAccount(id);
            response.put("success", true);
            response.put("message", "Compte verrouillé avec succès");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors du verrouillage : {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erreur lors du verrouillage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/admin/{id}/unlock", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Déverrouiller un compte", description = "Déverrouille le compte d'un utilisateur (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> unlockAccount(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            utilisateurService.unlockAccount(id);
            response.put("success", true);
            response.put("message", "Compte déverrouillé avec succès");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors du déverrouillage : {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erreur lors du déverrouillage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/admin/{id}/verify-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Vérifier email", description = "Marque l'email comme vérifié (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> verifyEmail(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            utilisateurService.verifyEmail(id);
            response.put("success", true);
            response.put("message", "Email vérifié avec succès");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification : {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erreur lors de la vérification");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= STATISTIQUES =================

    @GetMapping(value = "/stats/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Statistiques utilisateurs", description = "Récupère les statistiques des utilisateurs (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("totalActiveUsers", utilisateurService.countActiveUsers());
            stats.put("totalAdmins", utilisateurService.countUsersByRole(Utilisateur.Role.ADMIN));
            stats.put("totalRegularUsers", utilisateurService.countUsersByRole(Utilisateur.Role.USER));
            stats.put("newUsersLastWeek", utilisateurService.countNewUsersSince(LocalDateTime.now().minusWeeks(1)));
            stats.put("recentLoginsLastWeek", utilisateurService.countRecentLoginsSince(LocalDateTime.now().minusWeeks(1)));
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ================= MÉTHODES UTILITAIRES =================

    /**
     * Vérifie si l'utilisateur connecté est le propriétaire du compte ou un admin
     */
    public boolean isOwnerOrAdmin(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Vérifier si c'est un admin
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        // Vérifier si c'est le propriétaire
        String email = auth.getName();
        Optional<Utilisateur> userOpt = utilisateurService.findByEmail(email);
        
        return userOpt.isPresent() && userOpt.get().getIdUtilisateur().equals(userId);
    }
}