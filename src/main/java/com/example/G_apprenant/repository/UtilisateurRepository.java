package com.example.G_apprenant.repository;

import com.example.G_apprenant.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>, JpaSpecificationExecutor<Utilisateur> {
    
    // Recherches de base
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByEmailAndStatut(String email, Utilisateur.Statut statut);
    boolean existsByEmail(String email);
    
    // Recherches par rôle
    List<Utilisateur> findByRole(Utilisateur.Role role);
    List<Utilisateur> findByRoleAndStatut(Utilisateur.Role role, Utilisateur.Statut statut);
    
    // Recherches par statut
    List<Utilisateur> findByStatut(Utilisateur.Statut statut);
    List<Utilisateur> findByStatutNot(Utilisateur.Statut statut);
    
    // Recherches par nom et prénom
    List<Utilisateur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    List<Utilisateur> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
    
    // Recherches par date
    List<Utilisateur> findByDateCreationBetween(LocalDateTime debut, LocalDateTime fin);
    List<Utilisateur> findByDerniereConnexionBetween(LocalDateTime debut, LocalDateTime fin);
    
    // Recherches sur le compte
    List<Utilisateur> findByCompteVerrouille(Boolean verrouille);
    List<Utilisateur> findByEmailVerifie(Boolean verifie);
    
    // Recherches sur les tentatives de connexion
    List<Utilisateur> findByTentativesConnexionEchecGreaterThan(Integer tentatives);
    
    // Token de réinitialisation
    Optional<Utilisateur> findByTokenReinitialisation(String token);
    Optional<Utilisateur> findByTokenReinitialisationAndTokenExpirationAfter(String token, LocalDateTime now);
    
    // Requêtes personnalisées avec JPQL
    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email AND u.statut = 'ACTIF' AND u.compteVerrouille = false")
    Optional<Utilisateur> findActiveUserByEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.statut = 'ACTIF' AND u.derniereConnexion > :since")
    List<Utilisateur> findActiveUsersSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(u) FROM Utilisateur u WHERE u.role = :role AND u.statut = 'ACTIF'")
    Long countActiveUsersByRole(@Param("role") Utilisateur.Role role);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.statut = 'ACTIF' AND " +
           "(LOWER(u.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR " +
           "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :terme, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :terme, '%')))")
    List<Utilisateur> rechercherUtilisateurs(@Param("terme") String terme);
    
    // Requêtes de mise à jour
    @Modifying
    @Query("UPDATE Utilisateur u SET u.derniereConnexion = :dateConnexion, " +
           "u.tentativesConnexionEchec = 0, u.compteVerrouille = false WHERE u.id = :id")
    void updateSuccessfulLogin(@Param("id") Long id, @Param("dateConnexion") LocalDateTime dateConnexion);
    
    @Modifying
    @Query("UPDATE Utilisateur u SET u.tentativesConnexionEchec = u.tentativesConnexionEchec + 1, " +
           "u.compteVerrouille = CASE WHEN u.tentativesConnexionEchec >= 4 THEN true ELSE false END " +
           "WHERE u.email = :email")
    void incrementFailedLoginAttempts(@Param("email") String email);
    
    @Modifying
    @Query("UPDATE Utilisateur u SET u.tokenReinitialisation = :token, u.tokenExpiration = :expiration " +
           "WHERE u.email = :email")
    void setPasswordResetToken(@Param("email") String email, @Param("token") String token, 
                              @Param("expiration") LocalDateTime expiration);
    
    @Modifying
    @Query("UPDATE Utilisateur u SET u.motDePasseHash = :nouveauMotDePasse, " +
           "u.tokenReinitialisation = NULL, u.tokenExpiration = NULL WHERE u.tokenReinitialisation = :token")
    void resetPasswordWithToken(@Param("token") String token, @Param("nouveauMotDePasse") String nouveauMotDePasse);
    
    // Statistiques
    @Query("SELECT COUNT(u) FROM Utilisateur u WHERE u.statut = 'ACTIF'")
    Long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM Utilisateur u WHERE u.dateCreation >= :depuis")
    Long countNewUsersSince(@Param("depuis") LocalDateTime depuis);
    
    @Query("SELECT COUNT(u) FROM Utilisateur u WHERE u.derniereConnexion >= :depuis")
    Long countRecentLoginsSince(@Param("depuis") LocalDateTime depuis);
}
