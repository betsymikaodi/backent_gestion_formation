package com.example.G_apprenant.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.G_apprenant.entity.Apprenant;

public interface ApprenantRepository extends JpaRepository<Apprenant, Long>, JpaSpecificationExecutor<Apprenant> {
    
    // Recherches de base avec méthodes dérivées JPA (100% ORM)
    Optional<Apprenant> findByEmail(String email);
    Optional<Apprenant> findByCin(String cin);
    boolean existsByEmail(String email);
    boolean existsByCin(String cin);
    
    // Recherches par nom et prénom
    List<Apprenant> findByNomContainingIgnoreCase(String nom);
    List<Apprenant> findByPrenomContainingIgnoreCase(String prenom);
    List<Apprenant> findByNomIgnoreCase(String nom);
    List<Apprenant> findByPrenomIgnoreCase(String prenom);
    
    // Recherches combinées nom/prénom
    List<Apprenant> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(String nom, String prenom);
    List<Apprenant> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
    
    // Recherches par date de naissance
    List<Apprenant> findByDateNaissance(LocalDate dateNaissance);
    List<Apprenant> findByDateNaissanceBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Apprenant> findByDateNaissanceAfter(LocalDate date);
    List<Apprenant> findByDateNaissanceBefore(LocalDate date);
    
    // Recherches par téléphone et adresse
    List<Apprenant> findByTelephoneContaining(String telephone);
    List<Apprenant> findByAdresseContainingIgnoreCase(String adresse);
    
    // Recherches avec les inscriptions (relations)
    List<Apprenant> findByInscriptionsEmpty();
    List<Apprenant> findByInscriptionsIsNotEmpty();
    List<Apprenant> findByInscriptionsStatut(String statut);
    List<Apprenant> findByInscriptionsFormationIdFormation(Long formationId);
    List<Apprenant> findByInscriptionsFormationNomContainingIgnoreCase(String nomFormation);
    
    // Recherches avec tri
    List<Apprenant> findAllByOrderByNomAsc();
    List<Apprenant> findAllByOrderByPrenomAsc();
    List<Apprenant> findAllByOrderByDateNaissanceDesc();
    List<Apprenant> findByNomContainingIgnoreCaseOrderByPrenomAsc(String nom);
    
    // Recherches avec pagination
    Page<Apprenant> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    Page<Apprenant> findByPrenomContainingIgnoreCase(String prenom, Pageable pageable);
    Page<Apprenant> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    Page<Apprenant> findByDateNaissanceBetween(LocalDate dateDebut, LocalDate dateFin, Pageable pageable);
    
    // Compter les apprenants
    long countByNomContainingIgnoreCase(String nom);
    long countByInscriptionsStatut(String statut);
    long countByInscriptionsFormationIdFormation(Long formationId);
    long countByDateNaissanceBetween(LocalDate dateDebut, LocalDate dateFin);
    
    // Recherches par statut d'inscription avec tri
    List<Apprenant> findByInscriptionsStatutOrderByNomAsc(String statut);
    List<Apprenant> findDistinctByInscriptionsStatut(String statut);
    
    // Top apprenants
    List<Apprenant> findTop10ByOrderByIdApprenantDesc();
    
    // Recherches par période d'inscription
    List<Apprenant> findByInscriptionsDateInscription(LocalDate dateInscription);
    List<Apprenant> findByInscriptionsDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Apprenant> findByInscriptionsDateInscriptionAfter(LocalDate date);
    
    // Vérifications d'existence avec relations
    boolean existsByInscriptionsFormationIdFormation(Long formationId);
    boolean existsByInscriptionsStatut(String statut);
    
    // Note: Pour des filtres complexes multiples, nous utiliserons 
    // JpaSpecificationExecutor dans le service
}
