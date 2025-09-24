package com.example.G_apprenant.service.impl;

import com.example.G_apprenant.entity.ActivityLog;
import com.example.G_apprenant.exception.ResourceNotFoundException;
import com.example.G_apprenant.repository.ActivityLogRepository;
import com.example.G_apprenant.service.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public ActivityLog logActivity(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                                 String userEmail, String userName) {
        ActivityLog activity = new ActivityLog(entityType, action, entityId, userEmail);
        activity.setUserName(userName);
        return activityLogRepository.save(activity);
    }

    @Override
    public ActivityLog logActivity(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                                 String description, String userEmail, String userName) {
        ActivityLog activity = new ActivityLog(entityType, action, entityId, description, userEmail, userName);
        return activityLogRepository.save(activity);
    }

    @Override
    public ActivityLog logActivityWithDetails(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                                            String description, String userEmail, String userName, 
                                            String ipAddress, String userAgent) {
        ActivityLog activity = new ActivityLog(entityType, action, entityId, description, userEmail, userName);
        activity.setIpAddress(ipAddress);
        activity.setUserAgent(userAgent);
        return activityLogRepository.save(activity);
    }

    @Override
    public ActivityLog logActivityWithValues(ActivityLog.EntityType entityType, ActivityLog.Action action, Long entityId, 
                                           String description, String userEmail, String userName, 
                                           String oldValues, String newValues) {
        ActivityLog activity = new ActivityLog(entityType, action, entityId, description, userEmail, userName);
        activity.setOldValues(oldValues);
        activity.setNewValues(newValues);
        return activityLogRepository.save(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getAllActivities() {
        return activityLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getAllActivities(Pageable pageable) {
        return activityLogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getRecentActivities(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.findRecentActivities(since);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getRecentActivities(int hours, Pageable pageable) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.findRecentActivities(since, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByUser(String userEmail) {
        return activityLogRepository.findByUserEmail(userEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivitiesByUser(String userEmail, Pageable pageable) {
        return activityLogRepository.findByUserEmail(userEmail, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByEntityType(ActivityLog.EntityType entityType) {
        return activityLogRepository.findByEntityType(entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivitiesByEntityType(ActivityLog.EntityType entityType, Pageable pageable) {
        return activityLogRepository.findByEntityType(entityType, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByAction(ActivityLog.Action action) {
        return activityLogRepository.findByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivitiesByAction(ActivityLog.Action action, Pageable pageable) {
        return activityLogRepository.findByAction(action, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByEntity(ActivityLog.EntityType entityType, Long entityId) {
        return activityLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivitiesByEntity(ActivityLog.EntityType entityType, Long entityId, Pageable pageable) {
        return activityLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return activityLogRepository.findByTimestampBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivitiesInDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return activityLogRepository.findByTimestampBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getUserRecentActivities(String userEmail, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.findUserRecentActivities(userEmail, since);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getUserRecentActivities(String userEmail, int hours, Pageable pageable) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.findByUserEmailAndTimestampAfter(userEmail, since, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> searchActivitiesByDescription(String keyword, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.searchByDescription(keyword, since);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> searchActivitiesByDescription(String keyword, int hours, Pageable pageable) {
        // Implémentation personnalisée avec Specification si nécessaire
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<ActivityLog> results = activityLogRepository.searchByDescription(keyword, since);
        // Conversion en Page si nécessaire...
        return Page.empty(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalActivitiesCount() {
        return activityLogRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getRecentActivitiesCount(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.countActivitiesSince(since);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserActivitiesCount(String userEmail, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.countUserActivitiesSince(userEmail, since);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getEntityTypeActivitiesCount(ActivityLog.EntityType entityType, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.countActivitiesByTypeAndSince(entityType, since);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getActionActivitiesCount(ActivityLog.Action action, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return activityLogRepository.countActivitiesByActionAndSince(action, since);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getActivitiesSummaryByEntityType(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Object[]> results = activityLogRepository.getTopEntityTypesByActivity(since);
        Map<String, Long> summary = new HashMap<>();
        for (Object[] result : results) {
            ActivityLog.EntityType entityType = (ActivityLog.EntityType) result[0];
            Long count = (Long) result[1];
            summary.put(entityType.getDisplayName(), count);
        }
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getActivitiesSummaryByAction(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Object[]> results = activityLogRepository.getTopActionsByActivity(since);
        Map<String, Long> summary = new HashMap<>();
        for (Object[] result : results) {
            ActivityLog.Action action = (ActivityLog.Action) result[0];
            Long count = (Long) result[1];
            summary.put(action.getDisplayName(), count);
        }
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getActivitiesSummaryByUser(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Object[]> results = activityLogRepository.getTopUsersByActivity(since);
        return results.stream()
                .collect(Collectors.toMap(
                    result -> (String) result[0],
                    result -> (Long) result[1]
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> getActivitiesSummaryByHour(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Object[]> results = activityLogRepository.getActivitiesByHour(since);
        return results.stream()
                .collect(Collectors.toMap(
                    result -> (Integer) result[0],
                    result -> (Long) result[1]
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityLog getActivityById(Long id) {
        return activityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activité non trouvée avec l'ID: " + id));
    }

    @Override
    public void deleteActivity(Long id) {
        if (!activityLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activité non trouvée avec l'ID: " + id);
        }
        activityLogRepository.deleteById(id);
    }

    @Override
    public void deleteOldActivities(int daysOld) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysOld);
        activityLogRepository.deleteActivitiesOlderThan(beforeDate);
    }

    // Méthodes helper pour l'intégration avec les contrôleurs
    @Override
    public void logCreate(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName) {
        String description = String.format("Création de %s #%d", entityType.getDisplayName(), entityId);
        logActivity(entityType, ActivityLog.Action.CREATE, entityId, description, userEmail, userName);
    }

    @Override
    public void logUpdate(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName) {
        String description = String.format("Modification de %s #%d", entityType.getDisplayName(), entityId);
        logActivity(entityType, ActivityLog.Action.UPDATE, entityId, description, userEmail, userName);
    }

    @Override
    public void logDelete(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName) {
        String description = String.format("Suppression de %s #%d", entityType.getDisplayName(), entityId);
        logActivity(entityType, ActivityLog.Action.DELETE, entityId, description, userEmail, userName);
    }

    @Override
    public void logRead(ActivityLog.EntityType entityType, Long entityId, String userEmail, String userName) {
        String description = String.format("Consultation de %s #%d", entityType.getDisplayName(), entityId);
        logActivity(entityType, ActivityLog.Action.READ, entityId, description, userEmail, userName);
    }

    @Override
    public void logSearch(ActivityLog.EntityType entityType, String searchTerm, String userEmail, String userName) {
        String description = String.format("Recherche dans %s: '%s'", entityType.getDisplayName(), searchTerm);
        logActivity(entityType, ActivityLog.Action.SEARCH, 0L, description, userEmail, userName);
    }
}
