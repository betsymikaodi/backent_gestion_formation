package com.example.G_apprenant.service;

import com.example.G_apprenant.entity.Formation;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FormationService {

    // Méthodes CRUD de base
    Formation save(Formation formation);
    List<Formation> findAll();
    Page<Formation> findAll(Pageable pageable);
    Formation findById(Long id);
    Formation update(Long id, Formation formation);
    void deleteById(Long id);
    
    // Méthodes de recherche
    List<Formation> findByNomContaining(String nom);
    List<Formation> findByFraisBetween(BigDecimal fraisMin, BigDecimal fraisMax);
    List<Formation> findByFraisGreaterThanEqual(BigDecimal fraisMin);
    List<Formation> findByFraisLessThanEqual(BigDecimal fraisMax);
    List<Formation> findByDureeBetween(Integer dureeMin, Integer dureeMax);
    List<Formation> findByDureeGreaterThanEqual(Integer dureeMin);
    List<Formation> findByDureeLessThanEqual(Integer dureeMax);
    
    // Méthodes spécialisées
    List<Formation> findMostPopular(int limit);
    List<Formation> findCheapest(int limit);
    List<Formation> findWithAvailableSpots(int maxPlaces);
    
    // Statistiques
    long countConfirmedInscriptions(Long formationId);
    BigDecimal getAveragePrice();
    Double getAverageDuration();
}
