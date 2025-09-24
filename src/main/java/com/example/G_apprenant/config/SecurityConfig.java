package com.example.G_apprenant.config;

import com.example.G_apprenant.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF pour les API REST
                .csrf(AbstractHttpConfigurer::disable)
                // Activer la gestion CORS de Spring Security (utilise le bean CorsConfigurationSource)
                .cors(Customizer.withDefaults())
                
                // Configuration des autorisations
                .authorizeHttpRequests(authz -> authz
                        // Autoriser les preflight OPTIONS partout
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Routes publiques (authentification et documentation)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Lecture publique pour tests sur ces ressources
                        .requestMatchers(HttpMethod.GET, 
                                "/apprenants/**", 
                                "/formations/**", 
                                "/inscriptions/**", 
                                "/paiements/**").permitAll()

                        
                        // Routes d'administration (réservées aux ADMIN)
                        .requestMatchers("/utilisateurs/admin/**").hasRole("ADMIN")
                        .requestMatchers("/utilisateurs/stats/**").hasRole("ADMIN")
                        
                        // Autres routes nécessitent une authentification
                        .anyRequest().authenticated()
                )
                
                // Configuration de la session (stateless pour JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // Ajouter le filtre JWT avant le filtre d'authentification par défaut
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
