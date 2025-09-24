package com.example.G_apprenant.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.G_apprenant.entity.Formation;

public interface FormationRepository extends JpaRepository<Formation, Long>, JpaSpecificationExecutor<Formation> {
    
    // Recherches de base avec méthodes dérivées JPA (100% ORM)
    List<Formation> findByNomContainingIgnoreCase(String nom);
    Optional<Formation> findByNomIgnoreCase(String nom);
    boolean existsByNomIgnoreCase(String nom);
    
    // Recherches par durée
    List<Formation> findByDureeGreaterThanEqual(Integer dureeMin);
    List<Formation> findByDureeLessThanEqual(Integer dureeMax);
    List<Formation> findByDureeBetween(Integer dureeMin, Integer dureeMax);
    
    // Recherches par frais
    List<Formation> findByFraisLessThanEqual(BigDecimal fraisMax);
    List<Formation> findByFraisGreaterThanEqual(BigDecimal fraisMin);
    List<Formation> findByFraisBetween(BigDecimal fraisMin, BigDecimal fraisMax);
    
    // Tri automatique par différents critères
    List<Formation> findAllByOrderByFraisAsc();
    List<Formation> findAllByOrderByFraisDesc();
    List<Formation> findAllByOrderByDureeAsc();
    List<Formation> findAllByOrderByDureeDesc();
    List<Formation> findAllByOrderByNomAsc();
    List<Formation> findAllByOrderByDateNowDesc();
    
    // Recherches combinées avec tri
    List<Formation> findByNomContainingIgnoreCaseOrderByFraisAsc(String nom);
    List<Formation> findByFraisBetweenOrderByDureeAsc(BigDecimal fraisMin, BigDecimal fraisMax);
    List<Formation> findByDureeBetweenOrderByFraisAsc(Integer dureeMin, Integer dureeMax);
    
    // Recherches avec les relations (inscriptions)
    List<Formation> findByInscriptionsEmpty();
    List<Formation> findByInscriptionsIsNotEmpty();
    
    // Recherches par statut d'inscriptions
    List<Formation> findByInscriptionsStatut(String statut);
    List<Formation> findByInscriptionsStatutOrderByNomAsc(String statut);
    
    // Recherches avec pagination automatique
    Page<Formation> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    Page<Formation> findByFraisBetween(BigDecimal fraisMin, BigDecimal fraisMax, Pageable pageable);
    Page<Formation> findByDureeBetween(Integer dureeMin, Integer dureeMax, Pageable pageable);
    
    // Compter les formations
    long countByNomContainingIgnoreCase(String nom);
    long countByFraisBetween(BigDecimal fraisMin, BigDecimal fraisMax);
    long countByDureeBetween(Integer dureeMin, Integer dureeMax);
    long countByInscriptionsStatut(String statut);
    
    // Trouver les premières/dernières formations
    Optional<Formation> findFirstByOrderByFraisAsc();
    Optional<Formation> findFirstByOrderByFraisDesc();
    Optional<Formation> findFirstByOrderByDureeAsc();
    Optional<Formation> findFirstByOrderByDureeDesc();
    
    // Top formations
    List<Formation> findTop5ByOrderByFraisAsc();
    List<Formation> findTop10ByOrderByDureeDesc();
    
    // Note: Pour des recherches complexes avec filtres multiples,
    // nous utiliserons JpaSpecificationExecutor dans le service
}
