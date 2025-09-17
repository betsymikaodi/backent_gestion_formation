package com.example.G_apprenant.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.G_apprenant.entity.*;
import com.example.G_apprenant.exception.ResourceNotFoundException;
import com.example.G_apprenant.repository.*;
import com.example.G_apprenant.service.PaiementService;

@Service
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepo;
    private final InscriptionRepository inscriptionRepo;

    public PaiementServiceImpl(PaiementRepository paiementRepo, InscriptionRepository inscriptionRepo) {
        this.paiementRepo = paiementRepo;
        this.inscriptionRepo = inscriptionRepo;
    }

    @Override
    public Paiement create(Long inscriptionId, BigDecimal montant, String modePaiement, String module) {
        Inscription ins = inscriptionRepo.findById(inscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée id=" + inscriptionId));
        Paiement p = new Paiement();
        p.setInscription(ins);
        p.setMontant(montant);
        p.setModePaiement(modePaiement);
        p.setModule(module);
        Paiement saved = paiementRepo.save(p);

        // recalculer total payé pour l'inscription
        List<Paiement> paiements = paiementRepo.findByInscriptionIdInscription(inscriptionId);
        BigDecimal total = paiements.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal montantTotalAFinir = (ins.getFormation() != null && ins.getFormation().getFrais() != null)
                ? ins.getFormation().getFrais()
                : BigDecimal.ZERO;

        if (total.compareTo(montantTotalAFinir) >= 0) {
            ins.setStatut("Confirmé");
            inscriptionRepo.save(ins);
        }

        return saved;
    }

    @Override
    public List<Paiement> getByInscription(Long inscriptionId) {
        return paiementRepo.findByInscriptionIdInscription(inscriptionId);
    }
}
