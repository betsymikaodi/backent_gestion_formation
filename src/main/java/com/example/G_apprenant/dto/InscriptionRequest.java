package com.example.G_apprenant.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

public class InscriptionRequest {
    
    @NotNull(message = "L'ID de l'apprenant est obligatoire")
    private Long apprenantId;
    
    @NotNull(message = "L'ID de la formation est obligatoire")
    private Long formationId;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Le droit d'inscription ne peut pas être négatif")
    private BigDecimal droitInscription = BigDecimal.ZERO;

    // Constructeurs
    public InscriptionRequest() {}

    public InscriptionRequest(Long apprenantId, Long formationId, BigDecimal droitInscription) {
        this.apprenantId = apprenantId;
        this.formationId = formationId;
        this.droitInscription = droitInscription;
    }

    // Getters and Setters
    public Long getApprenantId() {
        return apprenantId;
    }

    public void setApprenantId(Long apprenantId) {
        this.apprenantId = apprenantId;
    }

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    public BigDecimal getDroitInscription() {
        return droitInscription;
    }

    public void setDroitInscription(BigDecimal droitInscription) {
        this.droitInscription = droitInscription;
    }

    @Override
    public String toString() {
        return "InscriptionRequest{" +
                "apprenantId=" + apprenantId +
                ", formationId=" + formationId +
                ", droitInscription=" + droitInscription +
                '}';
    }
}
