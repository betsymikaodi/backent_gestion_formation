package com.example.G_apprenant.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "inscription", indexes = {
        @Index(name = "idx_inscription_apprenant", columnList = "id_apprenant")
})
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscription")
    private Long idInscription;

    @NotNull(message = "L'apprenant est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_apprenant", nullable = false)
    @JsonBackReference
    private Apprenant apprenant;

    @NotNull(message = "La formation est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formation", nullable = false)
    @JsonBackReference
    private Formation formation;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    @Pattern(regexp = "En attente|Confirmé|Annulé", message = "Le statut doit être: En attente, Confirmé ou Annulé")
    @Column(length = 20)
    private String statut = "En attente"; // En attente / Confirmé / Annulé

    @DecimalMin(value = "0.0", inclusive = true, message = "Le droit d'inscription ne peut pas être négatif")
    @Column(name = "droit_inscription", precision = 10, scale = 2)
    private BigDecimal droitInscription = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "date_now", nullable = false, updatable = false)
    private LocalDateTime dateNow;

    @OneToMany(mappedBy = "inscription", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Paiement> paiements = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (dateInscription == null) dateInscription = LocalDate.now();
        if (droitInscription == null) droitInscription = BigDecimal.ZERO;
    }

    public Inscription() {}

    public Inscription(Apprenant apprenant, Formation formation) {
        this.apprenant = apprenant;
        this.formation = formation;
        this.dateInscription = LocalDate.now();
        this.statut = "En attente";
        this.droitInscription = BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getIdInscription() {
        return idInscription;
    }

    public void setIdInscription(Long idInscription) {
        this.idInscription = idInscription;
    }

    public Apprenant getApprenant() {
        return apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public BigDecimal getDroitInscription() {
        return droitInscription;
    }

    public void setDroitInscription(BigDecimal droitInscription) {
        this.droitInscription = droitInscription;
    }

    public Set<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Set<Paiement> paiements) {
        this.paiements = paiements;
    }

    public LocalDateTime getDateNow() {
        return dateNow;
    }

    // Méthodes utilitaires
    public void addPaiement(Paiement paiement) {
        paiements.add(paiement);
        paiement.setInscription(this);
    }

    public void removePaiement(Paiement paiement) {
        paiements.remove(paiement);
        paiement.setInscription(null);
    }

    public BigDecimal getMontantTotalPaye() {
        return paiements.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getMontantRestant() {
        BigDecimal fraisTotal = formation.getFrais().add(droitInscription);
        return fraisTotal.subtract(getMontantTotalPaye());
    }

    public boolean estEntierementPaye() {
        return getMontantRestant().compareTo(BigDecimal.ZERO) <= 0;
    }

    public void confirmer() {
        this.statut = "Confirmé";
    }

    public void annuler() {
        this.statut = "Annulé";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inscription)) return false;
        Inscription that = (Inscription) o;
        return idInscription != null && idInscription.equals(that.idInscription);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Inscription{" +
                "idInscription=" + idInscription +
                ", dateInscription=" + dateInscription +
                ", statut='" + statut + '\'' +
                ", droitInscription=" + droitInscription +
                '}';
    }
}
