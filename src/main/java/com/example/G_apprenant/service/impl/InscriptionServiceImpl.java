package com.example.G_apprenant.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.G_apprenant.entity.*;
import com.example.G_apprenant.exception.ResourceNotFoundException;
import com.example.G_apprenant.repository.*;
import com.example.G_apprenant.service.InscriptionService;

@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepo;
    private final ApprenantRepository apprenantRepo;
    private final FormationRepository formationRepo;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepo,
                                  ApprenantRepository apprenantRepo,
                                  FormationRepository formationRepo) {
        this.inscriptionRepo = inscriptionRepo;
        this.apprenantRepo = apprenantRepo;
        this.formationRepo = formationRepo;
    }

    @Override
    public Inscription enroll(Long apprenantId, Long formationId, BigDecimal droitInscription) {
        Apprenant a = apprenantRepo.findById(apprenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Apprenant non trouvé id=" + apprenantId));
        Formation f = formationRepo.findById(formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée id=" + formationId));
        Inscription ins = new Inscription();
        ins.setApprenant(a);
        ins.setFormation(f);
        ins.setDroitInscription(droitInscription != null ? droitInscription : BigDecimal.ZERO);
        ins.setStatut("En attente");
        return inscriptionRepo.save(ins);
    }

    @Override
    public Inscription getById(Long id) {
        return inscriptionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée id=" + id));
    }

    @Override
    public List<Inscription> getAll() {
        return inscriptionRepo.findAllByOrderByDateNowDesc();
    }

    @Override
    public List<Inscription> getByApprenant(Long apprenantId) {
        return inscriptionRepo.findByApprenantIdApprenant(apprenantId);
    }

    @Override
    public Inscription confirm(Long id) {
        Inscription i = getById(id);
        i.setStatut("Confirmé");
        return inscriptionRepo.save(i);
    }

    @Override
    public Inscription cancel(Long id) {
        Inscription i = getById(id);
        i.setStatut("Annulé");
        return inscriptionRepo.save(i);
    }



    @Override
    public Inscription update(Long id, BigDecimal droitInscription, String statut) {
        Inscription i = getById(id);
        if (droitInscription != null) {
            i.setDroitInscription(droitInscription);
        }
        if (statut != null && !statut.isBlank()) {
            if (!("En attente".equals(statut) || "Confirmé".equals(statut) || "Annulé".equals(statut))) {
                throw new IllegalArgumentException("Statut invalide: " + statut);
            }
            i.setStatut(statut);
        }
        return inscriptionRepo.save(i);
    }

    @Override
    public void delete(Long id) {
        // Vérifier l'existence avant suppression pour lever une 404 cohérente
        if (!inscriptionRepo.existsById(id)) {
            throw new ResourceNotFoundException("Inscription non trouvée id=" + id);
        }
        inscriptionRepo.deleteById(id);
    }
}
