package com.example.G_apprenant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log", indexes = {
        @Index(name = "idx_activity_log_timestamp", columnList = "timestamp"),
        @Index(name = "idx_activity_log_entity_type", columnList = "entity_type"),
        @Index(name = "idx_activity_log_action", columnList = "action"),
        @Index(name = "idx_activity_log_user_email", columnList = "user_email")
})
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_activity")
    private Long idActivity;

    @NotNull(message = "Le type d'entité est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 50)
    private EntityType entityType;

    @NotNull(message = "L'action est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Action action;

    @NotNull(message = "L'ID de l'entité est obligatoire")
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Size(max = 200, message = "La description ne peut pas dépasser 200 caractères")
    @Column(length = 200)
    private String description;

    @NotBlank(message = "L'email utilisateur est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @Size(max = 100, message = "Le nom utilisateur ne peut pas dépasser 100 caractères")
    @Column(name = "user_name", length = 100)
    private String userName;

    @NotNull(message = "Le timestamp est obligatoire")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues;

    // Enums
    public enum EntityType {
        APPRENANT("Apprenant"),
        FORMATION("Formation"),
        PAIEMENT("Paiement"),
        INSCRIPTION("Inscription"),
        UTILISATEUR("Utilisateur");

        private final String displayName;

        EntityType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Action {
        CREATE("Création"),
        READ("Consultation"),
        UPDATE("Modification"),
        DELETE("Suppression"),
        SEARCH("Recherche"),
        LOGIN("Connexion"),
        LOGOUT("Déconnexion");

        private final String displayName;

        Action(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructeurs
    public ActivityLog() {
        this.timestamp = LocalDateTime.now();
    }

    public ActivityLog(EntityType entityType, Action action, Long entityId, String userEmail) {
        this();
        this.entityType = entityType;
        this.action = action;
        this.entityId = entityId;
        this.userEmail = userEmail;
    }

    public ActivityLog(EntityType entityType, Action action, Long entityId, String description, 
                      String userEmail, String userName) {
        this(entityType, action, entityId, userEmail);
        this.description = description;
        this.userName = userName;
    }

    // Méthodes du cycle de vie JPA
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    // Getters et Setters
    public Long getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Long idActivity) {
        this.idActivity = idActivity;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    // Méthodes utilitaires
    public String getActivitySummary() {
        return String.format("%s - %s %s #%d par %s", 
                timestamp.toString(), 
                action.getDisplayName(), 
                entityType.getDisplayName(), 
                entityId, 
                userName != null ? userName : userEmail);
    }

    public boolean isRecentActivity(int hoursThreshold) {
        return timestamp.isAfter(LocalDateTime.now().minusHours(hoursThreshold));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityLog)) return false;
        ActivityLog that = (ActivityLog) o;
        return idActivity != null && idActivity.equals(that.idActivity);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "idActivity=" + idActivity +
                ", entityType=" + entityType +
                ", action=" + action +
                ", entityId=" + entityId +
                ", description='" + description + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
