package com.example.G_apprenant.specification;

import com.example.G_apprenant.dto.ApprenantSearchCriteria;
import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.entity.Inscription;
import com.example.G_apprenant.entity.Formation;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApprenantSpecification {
    
    /**
     * Crée une Specification pour les apprenants basée sur les critères de recherche
     */
    public static Specification<Apprenant> createSpecification(ApprenantSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Recherche par nom
            if (criteria.getNom() != null && !criteria.getNom().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nom")),
                    "%" + criteria.getNom().toLowerCase() + "%"
                ));
            }
            
            // Recherche par prénom
            if (criteria.getPrenom() != null && !criteria.getPrenom().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("prenom")),
                    "%" + criteria.getPrenom().toLowerCase() + "%"
                ));
            }
            
            // Recherche par email
            if (criteria.getEmail() != null && !criteria.getEmail().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + criteria.getEmail().toLowerCase() + "%"
                ));
            }
            
            // Recherche par téléphone
            if (criteria.getTelephone() != null && !criteria.getTelephone().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    root.get("telephone"),
                    "%" + criteria.getTelephone() + "%"
                ));
            }
            
            // Recherche par adresse
            if (criteria.getAdresse() != null && !criteria.getAdresse().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("adresse")),
                    "%" + criteria.getAdresse().toLowerCase() + "%"
                ));
            }
            
            // Recherche par CIN
            if (criteria.getCin() != null && !criteria.getCin().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    root.get("cin"),
                    "%" + criteria.getCin() + "%"
                ));
            }
            
            // Recherche par période de naissance
            if (criteria.getDateNaissanceDebut() != null && criteria.getDateNaissanceFin() != null) {
                predicates.add(criteriaBuilder.between(
                    root.get("dateNaissance"),
                    criteria.getDateNaissanceDebut(),
                    criteria.getDateNaissanceFin()
                ));
            } else if (criteria.getDateNaissanceDebut() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dateNaissance"),
                    criteria.getDateNaissanceDebut()
                ));
            } else if (criteria.getDateNaissanceFin() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("dateNaissance"),
                    criteria.getDateNaissanceFin()
                ));
            }
            
            // Recherche par statut d'inscription
            if (criteria.getStatutInscription() != null && !criteria.getStatutInscription().trim().isEmpty()) {
                Join<Apprenant, Inscription> inscriptionJoin = root.join("inscriptions", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(
                    inscriptionJoin.get("statut"),
                    criteria.getStatutInscription()
                ));
            }
            
            // Recherche par formation ID
            if (criteria.getFormationId() != null) {
                Join<Apprenant, Inscription> inscriptionJoin = root.join("inscriptions", JoinType.INNER);
                Join<Inscription, Formation> formationJoin = inscriptionJoin.join("formation", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(
                    formationJoin.get("idFormation"),
                    criteria.getFormationId()
                ));
            }
            
            // Recherche par nom de formation
            if (criteria.getNomFormation() != null && !criteria.getNomFormation().trim().isEmpty()) {
                Join<Apprenant, Inscription> inscriptionJoin = root.join("inscriptions", JoinType.INNER);
                Join<Inscription, Formation> formationJoin = inscriptionJoin.join("formation", JoinType.INNER);
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(formationJoin.get("nom")),
                    "%" + criteria.getNomFormation().toLowerCase() + "%"
                ));
            }
            
            // Recherche globale (nom, prénom, email)
            if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().trim().isEmpty()) {
                String searchTerm = criteria.getSearchTerm().toLowerCase();
                Predicate nomPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nom")),
                    "%" + searchTerm + "%"
                );
                Predicate prenomPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("prenom")),
                    "%" + searchTerm + "%"
                );
                Predicate emailPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + searchTerm + "%"
                );
                
                predicates.add(criteriaBuilder.or(nomPredicate, prenomPredicate, emailPredicate));
            }
            
            // Pour éviter les doublons dans les résultats avec les jointures
            if (query != null) {
                query.distinct(true);
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    /**
     * Specification pour rechercher par nom (contient, insensible à la casse)
     */
    public static Specification<Apprenant> hasNomContaining(String nom) {
        return (root, query, criteriaBuilder) -> {
            if (nom == null || nom.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("nom")),
                "%" + nom.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Specification pour rechercher par prénom (contient, insensible à la casse)
     */
    public static Specification<Apprenant> hasPrenomContaining(String prenom) {
        return (root, query, criteriaBuilder) -> {
            if (prenom == null || prenom.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("prenom")),
                "%" + prenom.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Specification pour rechercher par email (contient, insensible à la casse)
     */
    public static Specification<Apprenant> hasEmailContaining(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Specification pour rechercher par période de date de naissance
     */
    public static Specification<Apprenant> hasDateNaissanceBetween(LocalDate debut, LocalDate fin) {
        return (root, query, criteriaBuilder) -> {
            if (debut == null && fin == null) {
                return criteriaBuilder.conjunction();
            }
            if (debut != null && fin != null) {
                return criteriaBuilder.between(root.get("dateNaissance"), debut, fin);
            }
            if (debut != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateNaissance"), debut);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dateNaissance"), fin);
        };
    }
    
    /**
     * Specification pour rechercher par statut d'inscription
     */
    public static Specification<Apprenant> hasInscriptionStatut(String statut) {
        return (root, query, criteriaBuilder) -> {
            if (statut == null || statut.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Apprenant, Inscription> inscriptionJoin = root.join("inscriptions", JoinType.INNER);
            if (query != null) {
                query.distinct(true);
            }
            return criteriaBuilder.equal(inscriptionJoin.get("statut"), statut);
        };
    }
}
