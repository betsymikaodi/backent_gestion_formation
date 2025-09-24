package com.example.G_apprenant.service;

import com.example.G_apprenant.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ActivityLogService {
    
    // Enregistrement d'activités
    ActivityLog logActivity(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                           String userEmail, String userName);
    
    ActivityLog logActivity(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                           String description, String userEmail, String userName);
    
    ActivityLog logActivityWithDetails(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                                     String description, String userEmail, String userName, 
                                     String ipAddress, String userAgent);
    
    ActivityLog logActivityWithValues(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                                    String description, String userEmail, String userName, 
                                    String oldValues, String newValues);
    
    // Récupération d'activités
    List<ActivityLog> getAllActivities();
    Page<ActivityLog> getAllActivities(Pageable pageable);
    
    List<ActivityLog> getRecentActivities(int hours);
    Page<ActivityLog> getRecentActivities(int hours, Pageable pageable);
    
    List<ActivityLog> getActivitiesByUser(String userEmail);
    Page<ActivityLog> getActivitiesByUser(String userEmail, Pageable pageable);
    
    List<ActivityLog> getActivitiesByEntityType(ActivityLog.EntityType entityType);
    Page<ActivityLog> getActivitiesByEntityType(ActivityLog.EntityType entityType, Pageable pageable);
    
    List<ActivityLog> getActivitiesByAction(ActivityLog.Action action);
    Page<ActivityLog> getActivitiesByAction(ActivityLog.Action action, Pageable pageable);
    
    List<ActivityLog> getActivitiesByEntity(ActivityLog.EntityType entityType, Long entityId);
    Page<ActivityLog> getActivitiesByEntity(ActivityLog.EntityType entityType, Long entityId, Pageable pageable);
    
    List<ActivityLog> getActivitiesInDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Page<ActivityLog> getActivitiesInDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<ActivityLog> getUserRecentActivities(String userEmail, int hours);
    Page<ActivityLog> getUserRecentActivities(String userEmail, int hours, Pageable pageable);
    
    // Recherche
    List<ActivityLog> searchActivitiesByDescription(String keyword, int hours);
    Page<ActivityLog> searchActivitiesByDescription(String keyword, int hours, Pageable pageable);
    
    // Statistiques
    Long getTotalActivitiesCount();
    Long getRecentActivitiesCount(int hours);
    Long getUserActivitiesCount(String userEmail, int hours);
    Long getEntityTypeActivitiesCount(ActivityLog.EntityType entityType, int hours);
    Long getActionActivitiesCount(ActivityLog.Action action, int hours);
    
    Map<String, Long> getActivitiesSummaryByEntityType(int hours);
    Map<String, Long> getActivitiesSummaryByAction(int hours);
    Map<String, Long> getActivitiesSummaryByUser(int hours);
    Map<Integer, Long> getActivitiesSummaryByHour(int hours);
    
    // Utilitaires
    ActivityLog getActivityById(Long id);
    void deleteActivity(Long id);
    void deleteOldActivities(int daysOld);
    
    // Méthodes helper pour l'intégration avec les contrôleurs
    void logCreate(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName);
    void logUpdate(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName);
    void logDelete(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName);
    void logRead(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName);
    void logSearch(ActivityLog.EntityType entityType, String searchTerm, String userEmail, String userName);
}
