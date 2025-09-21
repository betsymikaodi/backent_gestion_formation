package com.example.G_apprenant.dto;

import java.math.BigDecimal;

public class PaiementUpdateRequest {
    private BigDecimal montant;
    private String modePaiement;
    private String module;

    public PaiementUpdateRequest() {}

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
}
