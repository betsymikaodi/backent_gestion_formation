package com.example.G_apprenant.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.G_apprenant.entity.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, Long>, JpaSpecificationExecutor<Paiement> {
    
    // Méthodes dérivées JPA de base (100% ORM)
    List<Paiement> findByInscriptionIdInscription(Long idInscription);
    
    // Recherches par mode de paiement
    List<Paiement> findByModePaiement(String modePaiement);
    List<Paiement> findByModePaiementOrderByDatePaiementDesc(String modePaiement);
    
    // Recherches par module
    List<Paiement> findByModule(String module);
    List<Paiement> findByModuleOrderByDatePaiementDesc(String module);
    
    // Recherches par date
    List<Paiement> findByDatePaiement(LocalDate datePaiement);
    List<Paiement> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Paiement> findByDatePaiementAfter(LocalDate date);
    List<Paiement> findByDatePaiementBefore(LocalDate date);
    
    // Recherches par montant
    List<Paiement> findByMontant(BigDecimal montant);
    List<Paiement> findByMontantGreaterThan(BigDecimal montantMin);
    List<Paiement> findByMontantLessThan(BigDecimal montantMax);
    List<Paiement> findByMontantBetween(BigDecimal montantMin, BigDecimal montantMax);
    
    // Recherches combinées
    List<Paiement> findByInscriptionIdInscriptionAndModePaiement(Long idInscription, String modePaiement);
    List<Paiement> findByInscriptionIdInscriptionAndModule(Long idInscription, String module);
    List<Paiement> findByModePaiementAndModule(String modePaiement, String module);
    List<Paiement> findByModePaiementAndDatePaiementBetween(String modePaiement, LocalDate dateDebut, LocalDate dateFin);
    
    // Recherches avec tri
    List<Paiement> findAllByOrderByDatePaiementDesc();
    List<Paiement> findAllByOrderByDateNowDesc();
    List<Paiement> findAllByOrderByMontantDesc();
    List<Paiement> findByInscriptionIdInscriptionOrderByDatePaiementDesc(Long idInscription);
    List<Paiement> findByInscriptionIdInscriptionOrderByMontantDesc(Long idInscription);
    
    // Recherches récentes
    List<Paiement> findByDatePaiementAfterOrderByDatePaiementDesc(LocalDate date);
    List<Paiement> findTop10ByOrderByDatePaiementDesc();
    List<Paiement> findTop5ByModePaiementOrderByDatePaiementDesc(String modePaiement);
    
    // Recherches avec pagination
    Page<Paiement> findByInscriptionIdInscription(Long idInscription, Pageable pageable);
    Page<Paiement> findByModePaiement(String modePaiement, Pageable pageable);
    Page<Paiement> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin, Pageable pageable);
    Page<Paiement> findByMontantBetween(BigDecimal montantMin, BigDecimal montantMax, Pageable pageable);
    
    // Compter les paiements
    long countByInscriptionIdInscription(Long idInscription);
    long countByModePaiement(String modePaiement);
    long countByModule(String module);
    long countByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin);
    long countByMontantGreaterThanEqual(BigDecimal montantMin);
    
    // Recherches par apprenant (via relation inscription)
    List<Paiement> findByInscriptionApprenantIdApprenant(Long idApprenant);
    List<Paiement> findByInscriptionApprenantNomContainingIgnoreCase(String nom);
    
    // Recherches par formation (via relation inscription)
    List<Paiement> findByInscriptionFormationIdFormation(Long idFormation);
    List<Paiement> findByInscriptionFormationNomContainingIgnoreCase(String nomFormation);
    
    // Vérifications d'existence
    boolean existsByInscriptionIdInscription(Long idInscription);
    boolean existsByModePaiement(String modePaiement);
    boolean existsByInscriptionIdInscriptionAndModule(Long idInscription, String module);
    
    // Recherches par statut d'inscription
    List<Paiement> findByInscriptionStatut(String statutInscription);
    List<Paiement> findByInscriptionStatutOrderByDatePaiementDesc(String statutInscription);
    
    // Première et dernière paiements
    Optional<Paiement> findFirstByOrderByDatePaiementAsc();
    Optional<Paiement> findFirstByOrderByDatePaiementDesc();
    Optional<Paiement> findFirstByInscriptionIdInscriptionOrderByDatePaiementDesc(Long idInscription);
    
    // Recherches par période et mode de paiement
    List<Paiement> findByDatePaiementBetweenAndModePaiement(LocalDate dateDebut, LocalDate dateFin, String modePaiement);
    
    // Recherches distinctes
    List<Paiement> findDistinctByInscriptionIdInscriptionAndModule(Long idInscription, String module);
    
    // Top montants
    List<Paiement> findTop10ByOrderByMontantDesc();
    List<Paiement> findTop5ByInscriptionIdInscriptionOrderByMontantDesc(Long idInscription);
    
    // Recherches par modules spécifiques
    List<Paiement> findByModuleIn(List<String> modules);
    List<Paiement> findByModePaiementIn(List<String> modesPaiement);
    
    // Note: Pour des calculs de sommes, moyennes et statistiques complexes,
    // nous utiliserons des méthodes dans le service avec les données récupérées
}
