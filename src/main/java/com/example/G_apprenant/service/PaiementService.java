package com.example.G_apprenant.service;

import com.example.G_apprenant.entity.Paiement;
import java.util.List;

public interface PaiementService {
    Paiement create(Long inscriptionId, java.math.BigDecimal montant, String modePaiement, String module);
    List<Paiement> getByInscription(Long inscriptionId);
}