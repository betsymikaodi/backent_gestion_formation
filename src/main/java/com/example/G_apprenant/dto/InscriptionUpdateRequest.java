package com.example.G_apprenant.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public class InscriptionUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "Le droit d'inscription ne peut pas être négatif")
    private BigDecimal droitInscription;

    @Pattern(regexp = "En attente|Confirmé|Annulé", message = "Le statut doit être: En attente, Confirmé ou Annulé")
    private String statut;

    public InscriptionUpdateRequest() {}

    public BigDecimal getDroitInscription() { return droitInscription; }
    public void setDroitInscription(BigDecimal droitInscription) { this.droitInscription = droitInscription; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
