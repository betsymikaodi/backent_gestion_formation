package com.example.G_apprenant.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "paiement", indexes = {
        @Index(name = "idx_paiement_inscription", columnList = "id_inscription")
})
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement")
    private Long idPaiement;

    @NotNull(message = "L'inscription est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscription", nullable = false)
    @JsonBackReference
    private Inscription inscription;

    @Column(name = "date_paiement")
    private LocalDate datePaiement;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Pattern(regexp = "Espèce|Virement|Mobile Money", message = "Le mode de paiement doit être: Espèce, Virement ou Mobile Money")
    @Column(name = "mode_paiement", length = 20)
    private String modePaiement = "Espèce";

    @Pattern(regexp = "Module [1-4]", message = "Le module doit être: Module 1, Module 2, Module 3 ou Module 4")
    @Column(length = 20)
    private String module = "Module 1";

    @CreationTimestamp
    @Column(name = "date_now", nullable = false, updatable = false)
    private LocalDateTime dateNow;

    @PrePersist
    public void prePersist() {
        if (datePaiement == null) datePaiement = LocalDate.now();
    }

    public Paiement() {}

    public Paiement(Inscription inscription, BigDecimal montant, String modePaiement, String module) {
        this.inscription = inscription;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.module = module;
        this.datePaiement = LocalDate.now();
    }

    // Getters and Setters
    public Long getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
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

    public LocalDateTime getDateNow() {
        return dateNow;
    }

    // Méthodes utilitaires
    public boolean estVirement() {
        return "Virement".equals(modePaiement);
    }

    public boolean estMobileMoney() {
        return "Mobile Money".equals(modePaiement);
    }

    public boolean estEspece() {
        return "Espèce".equals(modePaiement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paiement)) return false;
        Paiement paiement = (Paiement) o;
        return idPaiement != null && idPaiement.equals(paiement.idPaiement);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement=" + idPaiement +
                ", datePaiement=" + datePaiement +
                ", montant=" + montant +
                ", modePaiement='" + modePaiement + '\'' +
                ", module='" + module + '\'' +
                '}';
    }
}
