package com.example.G_apprenant.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public class PaiementRequest {
    
    @NotNull(message = "L'ID de l'inscription est obligatoire")
    private Long inscriptionId;
    
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    private BigDecimal montant;
    
    @Pattern(regexp = "Espèce|Virement|Mobile Money", 
             message = "Le mode de paiement doit être: Espèce, Virement ou Mobile Money")
    private String modePaiement = "Espèce";
    
    @Pattern(regexp = "Module [1-4]", 
             message = "Le module doit être: Module 1, Module 2, Module 3 ou Module 4")
    private String module = "Module 1";

    // Constructeurs
    public PaiementRequest() {}

    public PaiementRequest(Long inscriptionId, BigDecimal montant, String modePaiement, String module) {
        this.inscriptionId = inscriptionId;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.module = module;
    }

    // Getters and Setters
    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "PaiementRequest{" +
                "inscriptionId=" + inscriptionId +
                ", montant=" + montant +
                ", modePaiement='" + modePaiement + '\'' +
                ", module='" + module + '\'' +
                '}';
    }
}
