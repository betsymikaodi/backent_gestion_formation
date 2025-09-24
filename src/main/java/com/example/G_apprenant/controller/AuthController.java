package com.example.G_apprenant.controller;

import com.example.G_apprenant.dto.LoginRequest;
import com.example.G_apprenant.dto.LoginResponse;
import com.example.G_apprenant.dto.UtilisateurRequest;
import com.example.G_apprenant.entity.Utilisateur;
import com.example.G_apprenant.security.JwtService;
import com.example.G_apprenant.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs pour l'authentification et la gestion des sessions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;

    public AuthController(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Connexion utilisateur", description = "Authentification d'un utilisateur avec email et mot de passe")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Tentative de connexion pour l'email : {}", request.getEmail());
        
        try {
            Optional<Utilisateur> userOpt = utilisateurService.authenticate(request.getEmail(), request.getMotDePasse());
            
            if (userOpt.isEmpty()) {
                logger.warn("Échec de l'authentification pour l'email : {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(null, null, null, "Email ou mot de passe incorrect", false));
            }
            
            Utilisateur user = userOpt.get();
            
            // Générer le token JWT avec les claims personnalisés
            String token = jwtService.generateTokenWithClaims(
                    user.getEmail(), 
                    user.getIdUtilisateur(), 
                    user.getRole().name()
            );
            
            logger.info("Connexion réussie pour l'utilisateur : {} (ID: {})", user.getEmail(), user.getIdUtilisateur());
            
            return ResponseEntity.ok(new LoginResponse(
                    token,
                    user.getRole().name(),
                    user.getNomComplet(),
                    "Connexion réussie",
                    true
            ));
            
        } catch (Exception e) {
            logger.error("Erreur lors de la connexion : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(null, null, null, "Erreur interne du serveur", false));
        }
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Inscription utilisateur", description = "Création d'un nouveau compte utilisateur")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UtilisateurRequest request) {
        logger.info("Tentative d'inscription pour l'email : {}", request.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Vérifier si l'email existe déjà
            if (utilisateurService.existsByEmail(request.getEmail())) {
                logger.warn("Tentative d'inscription avec un email déjà existant : {}", request.getEmail());
                response.put("success", false);
                response.put("message", "Un utilisateur avec cet email existe déjà");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Créer le nouvel utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(request.getEmail());
            utilisateur.setMotDePasseHash(request.getMotDePasse()); // Sera hashé dans le service
            utilisateur.setPrenom(request.getPrenom());
            utilisateur.setNom(request.getNom());
            utilisateur.setTelephone(request.getTelephone());
            utilisateur.setRole(request.getRole() != null ? request.getRole() : Utilisateur.Role.USER);
            utilisateur.setPreferencesLangue(request.getPreferencesLangue());
            utilisateur.setThemePreference(request.getThemePreference());
            
            Utilisateur savedUser = utilisateurService.create(utilisateur);
            
            logger.info("Inscription réussie pour l'utilisateur : {} (ID: {})", savedUser.getEmail(), savedUser.getIdUtilisateur());
            
            response.put("success", true);
            response.put("message", "Inscription réussie");
            response.put("userId", savedUser.getIdUtilisateur());
            response.put("email", savedUser.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Erreur de validation lors de l'inscription : {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'inscription : {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renouvellement du token", description = "Renouvelle un token JWT valide")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new LoginResponse(null, null, null, "Token manquant ou invalide", false));
            }
            
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);
            
            if (email == null || !jwtService.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(null, null, null, "Token invalide", false));
            }
            
            Optional<Utilisateur> userOpt = utilisateurService.findActiveUserByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(null, null, null, "Utilisateur non trouvé", false));
            }
            
            Utilisateur user = userOpt.get();
            String newToken = jwtService.generateTokenWithClaims(
                    user.getEmail(), 
                    user.getIdUtilisateur(), 
                    user.getRole().name()
            );
            
            return ResponseEntity.ok(new LoginResponse(
                    newToken,
                    user.getRole().name(),
                    user.getNomComplet(),
                    "Token renouvelé avec succès",
                    true
            ));
            
        } catch (Exception e) {
            logger.error("Erreur lors du renouvellement du token : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(null, null, null, "Erreur interne du serveur", false));
        }
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Mot de passe oublié", description = "Initie la procédure de réinitialisation du mot de passe")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            utilisateurService.resetPasswordRequest(email);
            
            response.put("success", true);
            response.put("message", "Si cet email existe, vous recevrez un lien de réinitialisation");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la demande de réinitialisation : {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Réinitialisation du mot de passe", description = "Réinitialise le mot de passe avec un token valide")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @Parameter(description = "Token de réinitialisation") @RequestParam String token,
            @Parameter(description = "Nouveau mot de passe") @RequestParam String newPassword) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (newPassword == null || newPassword.trim().length() < 6) {
                response.put("success", false);
                response.put("message", "Le mot de passe doit contenir au moins 6 caractères");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            utilisateurService.resetPasswordWithToken(token, newPassword);
            
            response.put("success", true);
            response.put("message", "Mot de passe réinitialisé avec succès");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la réinitialisation du mot de passe : {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Erreur interne du serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Validation du token", description = "Vérifie la validité d'un token JWT")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("valid", false);
                response.put("message", "Token manquant ou format invalide");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            String token = authHeader.substring(7);
            boolean isValid = jwtService.isValidToken(token);
            
            if (isValid) {
                String email = jwtService.extractEmail(token);
                String role = jwtService.extractRole(token);
                Long userId = jwtService.extractUserId(token);
                
                response.put("valid", true);
                response.put("email", email);
                response.put("role", role);
                response.put("userId", userId);
                response.put("message", "Token valide");
            } else {
                response.put("valid", false);
                response.put("message", "Token invalide ou expiré");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du token : {}", e.getMessage(), e);
            response.put("valid", false);
            response.put("message", "Erreur lors de la validation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}