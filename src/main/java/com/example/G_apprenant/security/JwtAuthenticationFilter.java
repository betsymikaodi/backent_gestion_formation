package com.example.G_apprenant.security;

import com.example.G_apprenant.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtService jwtService;
    private final UtilisateurService utilisateurService;

    public JwtAuthenticationFilter(JwtService jwtService, @Lazy UtilisateurService utilisateurService) {
        this.jwtService = jwtService;
        this.utilisateurService = utilisateurService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Vérifier si le header Authorization est présent et commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraire le token JWT
        jwt = authHeader.substring(7);
        
        try {
            userEmail = jwtService.extractEmail(jwt);

            // Si l'email est présent et qu'aucune authentification n'est déjà définie
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Vérifier si l'utilisateur existe et est actif
                Optional<com.example.G_apprenant.entity.Utilisateur> userOpt = 
                    utilisateurService.findActiveUserByEmail(userEmail);
                
                if (userOpt.isPresent()) {
                    com.example.G_apprenant.entity.Utilisateur user = userOpt.get();
                    
                    // Valider le token
                    if (jwtService.validateToken(jwt, userEmail)) {
                        
                        // Extraire le rôle du token ou de l'utilisateur
                        String role = jwtService.extractRole(jwt);
                        if (role == null) {
                            role = user.getRole().name();
                        }
                        
                        // Créer les authorities
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + role)
                        );
                        
                        // Créer le token d'authentification
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                authorities
                        );
                        
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        // Définir l'authentification dans le contexte de sécurité
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        logger.debug("Utilisateur authentifié avec succès : {}", userEmail);
                    } else {
                        logger.warn("Token JWT invalide pour l'utilisateur : {}", userEmail);
                    }
                } else {
                    logger.warn("Utilisateur non trouvé ou inactif : {}", userEmail);
                }
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification JWT : {}", e.getMessage());
            // Ne pas bloquer la requête, laisser Spring Security gérer l'absence d'authentification
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        
        // Ne pas filtrer les routes publiques
        return path.startsWith("/auth/") || 
               path.startsWith("/swagger-ui/") || 
               path.startsWith("/v3/api-docs/") ||
               path.equals("/swagger-ui.html") ||
               path.startsWith("/actuator/health");
    }
}