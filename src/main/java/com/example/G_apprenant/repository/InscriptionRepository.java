package com.example.G_apprenant.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.G_apprenant.entity.Inscription;

public interface InscriptionRepository extends JpaRepository<Inscription, Long>, JpaSpecificationExecutor<Inscription> {
    
    // Méthodes dérivées JPA de base (100% ORM)
    List<Inscription> findByApprenantIdApprenant(Long idApprenant);
    List<Inscription> findByFormationIdFormation(Long idFormation);
    List<Inscription> findByStatut(String statut);
    
    // Recherches par date
    List<Inscription> findByDateInscription(LocalDate date);
    List<Inscription> findByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Inscription> findByDateInscriptionAfter(LocalDate date);
    List<Inscription> findByDateInscriptionBefore(LocalDate date);
    
    // Recherches combinées
    List<Inscription> findByApprenantIdApprenantAndStatut(Long idApprenant, String statut);
    List<Inscription> findByFormationIdFormationAndStatut(Long idFormation, String statut);
    Optional<Inscription> findByApprenantIdApprenantAndFormationIdFormation(Long idApprenant, Long idFormation);
    
    // Recherches par droit d'inscription
    List<Inscription> findByDroitInscription(BigDecimal droitInscription);
    List<Inscription> findByDroitInscriptionGreaterThan(BigDecimal montant);
    List<Inscription> findByDroitInscriptionBetween(BigDecimal min, BigDecimal max);
    
    // Vérifications d'existence
    boolean existsByApprenantIdApprenantAndFormationIdFormation(Long idApprenant, Long idFormation);
    boolean existsByApprenantIdApprenantAndFormationIdFormationAndStatut(Long idApprenant, Long idFormation, String statut);
    boolean existsByStatut(String statut);
    
    // Recherches avec tri
    List<Inscription> findAllByOrderByDateInscriptionDesc();
    List<Inscription> findAllByOrderByDateInscriptionAsc();
    List<Inscription> findAllByOrderByDateNowDesc();
    List<Inscription> findByStatutOrderByDateInscriptionDesc(String statut);
    List<Inscription> findByApprenantIdApprenantOrderByDateInscriptionDesc(Long idApprenant);
    
    // Recherches récentes
    List<Inscription> findByDateInscriptionAfterOrderByDateInscriptionDesc(LocalDate date);
    List<Inscription> findTop10ByOrderByDateInscriptionDesc();
    List<Inscription> findTop5ByStatutOrderByDateInscriptionDesc(String statut);
    
    // Recherches avec pagination
    Page<Inscription> findByStatut(String statut, Pageable pageable);
    Page<Inscription> findByApprenantIdApprenant(Long idApprenant, Pageable pageable);
    Page<Inscription> findByFormationIdFormation(Long idFormation, Pageable pageable);
    Page<Inscription> findByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin, Pageable pageable);
    
    // Compter les inscriptions
    long countByStatut(String statut);
    long countByApprenantIdApprenant(Long idApprenant);
    long countByFormationIdFormation(Long idFormation);
    long countByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);
    long countByFormationIdFormationAndStatut(Long idFormation, String statut);
    
    // Recherches par nom d'apprenant ou formation (via relations)
    List<Inscription> findByApprenantNomContainingIgnoreCase(String nom);
    List<Inscription> findByApprenantPrenomContainingIgnoreCase(String prenom);
    List<Inscription> findByFormationNomContainingIgnoreCase(String nomFormation);
    
    // Recherches avec relations et paiements
    List<Inscription> findByPaiementsEmpty();
    List<Inscription> findByPaiementsIsNotEmpty();
    List<Inscription> findByPaiementsModePaiement(String modePaiement);
    
    // Recherches par période spécifique
    List<Inscription> findByDateInscriptionAfterAndDateInscriptionBefore(LocalDate dateDebut, LocalDate dateFin);
    
    // Recherches distinctes
    List<Inscription> findDistinctByApprenantIdApprenantAndStatut(Long idApprenant, String statut);
    List<Inscription> findDistinctByFormationIdFormationAndStatut(Long idFormation, String statut);
    
    // Recherches par statut avec différents tris
    List<Inscription> findByStatutOrderByApprenantNomAsc(String statut);
    List<Inscription> findByStatutOrderByFormationNomAsc(String statut);
    
    // Première et dernière inscription
    Optional<Inscription> findFirstByOrderByDateInscriptionAsc();
    Optional<Inscription> findFirstByOrderByDateInscriptionDesc();
    Optional<Inscription> findFirstByApprenantIdApprenantOrderByDateInscriptionDesc(Long idApprenant);
    
    // Recherches avec montants
    List<Inscription> findByDroitInscriptionGreaterThanEqual(BigDecimal montantMin);
    List<Inscription> findByDroitInscriptionLessThanEqual(BigDecimal montantMax);
    
    // Note: Pour des recherches complexes avec calculs de montants payés/restants,
    // nous utiliserons JpaSpecificationExecutor et des méthodes dans le service
}
