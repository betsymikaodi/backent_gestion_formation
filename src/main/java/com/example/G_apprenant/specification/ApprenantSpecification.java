package com.example.G_apprenant.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.entity.Inscription;

public class ApprenantSpecification {

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

    public static Specification<Apprenant> hasAgeBetween(Integer ageMin, Integer ageMax) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (ageMin != null) {
                LocalDate dateMax = LocalDate.now().minusYears(ageMin);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateNaissance"), dateMax));
            }
            
            if (ageMax != null) {
                LocalDate dateMin = LocalDate.now().minusYears(ageMax + 1).plusDays(1);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateNaissance"), dateMin));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Apprenant> hasDateNaissanceBetween(LocalDate dateDebut, LocalDate dateFin) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (dateDebut != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateNaissance"), dateDebut));
            }
            
            if (dateFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateNaissance"), dateFin));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Apprenant> hasInscriptionWithStatut(String statut) {
        return (root, query, criteriaBuilder) -> {
            if (statut == null || statut.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            Join<Apprenant, Inscription> inscriptions = root.join("inscriptions");
            return criteriaBuilder.equal(inscriptions.get("statut"), statut);
        };
    }

    public static Specification<Apprenant> hasInscriptionInFormation(Long formationId) {
        return (root, query, criteriaBuilder) -> {
            if (formationId == null) {
                return criteriaBuilder.conjunction();
            }
            
            Join<Apprenant, Inscription> inscriptions = root.join("inscriptions");
            return criteriaBuilder.equal(inscriptions.get("formation").get("idFormation"), formationId);
        };
    }

    public static Specification<Apprenant> hasInscriptionBetweenDates(LocalDate dateDebut, LocalDate dateFin) {
        return (root, query, criteriaBuilder) -> {
            if (dateDebut == null && dateFin == null) {
                return criteriaBuilder.conjunction();
            }
            
            Join<Apprenant, Inscription> inscriptions = root.join("inscriptions");
            List<Predicate> predicates = new ArrayList<>();
            
            if (dateDebut != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(inscriptions.get("dateInscription"), dateDebut));
            }
            
            if (dateFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(inscriptions.get("dateInscription"), dateFin));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Apprenant> hasNomCompletContaining(String nomComplet) {
        return (root, query, criteriaBuilder) -> {
            if (nomComplet == null || nomComplet.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            String searchTerm = "%" + nomComplet.toLowerCase() + "%";
            return criteriaBuilder.like(
                criteriaBuilder.lower(
                    criteriaBuilder.concat(
                        criteriaBuilder.concat(root.get("prenom"), " "), 
                        root.get("nom")
                    )
                ), 
                searchTerm
            );
        };
    }

    // Méthode utilitaire pour combiner plusieurs spécifications
    public static Specification<Apprenant> withFilters(String nom, String prenom, String email, 
                                                       Integer ageMin, Integer ageMax, String statut) {
        return Specification.where(hasNomContaining(nom))
                .and(hasPrenomContaining(prenom))
                .and(hasEmailContaining(email))
                .and(hasAgeBetween(ageMin, ageMax))
                .and(hasInscriptionWithStatut(statut));
    }
}