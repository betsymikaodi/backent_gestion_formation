package com.example.G_apprenant.service;

import java.util.List;
import com.example.G_apprenant.entity.Inscription;
import java.math.BigDecimal;

public interface InscriptionService {
    Inscription enroll(Long apprenantId, Long formationId, BigDecimal droitInscription);
    Inscription getById(Long id);
    List<Inscription> getAll();
    List<Inscription> getByApprenant(Long apprenantId);
    Inscription confirm(Long id);
    Inscription cancel(Long id);
    Inscription update(Long id, java.math.BigDecimal droitInscription, String statut);
    void delete(Long id);
}
