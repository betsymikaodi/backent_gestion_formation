package com.example.G_apprenant.service.impl;

import com.example.G_apprenant.entity.Formation;
import com.example.G_apprenant.repository.FormationRepository;
import com.example.G_apprenant.service.FormationService;
import com.example.G_apprenant.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FormationServiceImpl implements FormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Override
    public Formation save(Formation formation) {
        return formationRepository.save(formation);
    }

    @Override
    public List<Formation> findAll() {
        return formationRepository.findAllByOrderByDateNowDesc();
    }

    @Override
    public Page<Formation> findAll(Pageable pageable) {
        return formationRepository.findAll(pageable);
    }

    @Override
    public Formation findById(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée avec ID : " + id));
    }

    @Override
    public Formation update(Long id, Formation formation) {
        return formationRepository.findById(id)
                .map(existingFormation -> {
                    if (formation.getNom() != null) {
                        existingFormation.setNom(formation.getNom());
                    }
                    if (formation.getDescription() != null) {
                        existingFormation.setDescription(formation.getDescription());
                    }
                    if (formation.getDuree() != null) {
                        existingFormation.setDuree(formation.getDuree());
                    }
                    if (formation.getFrais() != null) {
                        existingFormation.setFrais(formation.getFrais());
                    }
                    return formationRepository.save(existingFormation);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée avec ID : " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!formationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Formation non trouvée avec ID : " + id);
        }
        formationRepository.deleteById(id);
    }

    // Méthodes de recherche
    @Override
    public List<Formation> findByNomContaining(String nom) {
        return formationRepository.findByNomContainingIgnoreCase(nom);
    }

    @Override
    public List<Formation> findByFraisBetween(BigDecimal fraisMin, BigDecimal fraisMax) {
        return formationRepository.findByFraisBetween(fraisMin, fraisMax);
    }

    @Override
    public List<Formation> findByFraisGreaterThanEqual(BigDecimal fraisMin) {
        return formationRepository.findByFraisGreaterThanEqual(fraisMin);
    }

    @Override
    public List<Formation> findByFraisLessThanEqual(BigDecimal fraisMax) {
        return formationRepository.findByFraisLessThanEqual(fraisMax);
    }

    @Override
    public List<Formation> findByDureeBetween(Integer dureeMin, Integer dureeMax) {
        return formationRepository.findByDureeBetween(dureeMin, dureeMax);
    }

    @Override
    public List<Formation> findByDureeGreaterThanEqual(Integer dureeMin) {
        return formationRepository.findByDureeGreaterThanEqual(dureeMin);
    }

    @Override
    public List<Formation> findByDureeLessThanEqual(Integer dureeMax) {
        return formationRepository.findByDureeLessThanEqual(dureeMax);
    }

    // Méthodes spécialisées  
    @Override
    public List<Formation> findMostPopular(int limit) {
        // Pour le moment, on retourne par nom pour simplifier
        // Plus tard, on pourra implémenter une logique avec comptage des inscriptions
        List<Formation> allFormations = formationRepository.findAllByOrderByNomAsc();
        return allFormations.stream().limit(limit).toList();
    }

    @Override
    public List<Formation> findCheapest(int limit) {
        return formationRepository.findTop5ByOrderByFraisAsc();
    }

    @Override
    public List<Formation> findWithAvailableSpots(int maxPlaces) {
        // Pour le moment, on retourne toutes les formations
        // Plus tard, on pourra implémenter une logique avec comptage des inscriptions
        return formationRepository.findByInscriptionsEmpty();
    }

    // Statistiques (simplifiées pour la version ORM pure)
    @Override
    public long countConfirmedInscriptions(Long formationId) {
        return formationRepository.countByInscriptionsStatut("Confirmé");
    }

    @Override
    public BigDecimal getAveragePrice() {
        // Calcul manuel pour éviter les requêtes SQL
        List<Formation> formations = formationRepository.findAll();
        if (formations.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = formations.stream()
                .map(Formation::getFrais)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        return sum.divide(BigDecimal.valueOf(formations.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    @Override
    public Double getAverageDuration() {
        // Calcul manuel pour éviter les requêtes SQL
        List<Formation> formations = formationRepository.findAll();
        if (formations.isEmpty()) {
            return 0.0;
        }
        
        double sum = formations.stream()
                .mapToInt(Formation::getDuree)
                .average()
                .orElse(0.0);
                
        return sum;
    }
}
