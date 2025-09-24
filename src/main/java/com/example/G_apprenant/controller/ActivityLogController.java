package com.example.G_apprenant.controller;

import com.example.G_apprenant.entity.ActivityLog;
import com.example.G_apprenant.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
@Tag(name = "Activity Log Management", description = "APIs pour la gestion des activités récentes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    // =================== CONSULTATION DES ACTIVITÉS ===================

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir toutes les activités avec pagination")
    public ResponseEntity<Page<ActivityLog>> getAllActivities(
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size,
            @RequestParam(defaultValue = "timestamp") @Parameter(description = "Champ de tri") String sortBy,
            @RequestParam(defaultValue = "desc") @Parameter(description = "Direction du tri") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ActivityLog> activities = activityLogService.getAllActivities(pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités récentes")
    public ResponseEntity<List<ActivityLog>> getRecentActivities(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        List<ActivityLog> activities = activityLogService.getRecentActivities(hours);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/recent/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités récentes avec pagination")
    public ResponseEntity<Page<ActivityLog>> getRecentActivitiesPaginated(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getRecentActivities(hours, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/user/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités par utilisateur")
    public ResponseEntity<Page<ActivityLog>> getActivitiesByUser(
            @PathVariable @Parameter(description = "Email de l'utilisateur") String userEmail,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByUser(userEmail, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/user/{userEmail}/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités récentes d'un utilisateur")
    public ResponseEntity<List<ActivityLog>> getUserRecentActivities(
            @PathVariable @Parameter(description = "Email de l'utilisateur") String userEmail,
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        List<ActivityLog> activities = activityLogService.getUserRecentActivities(userEmail, hours);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/entity/{entityType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités par type d'entité")
    public ResponseEntity<Page<ActivityLog>> getActivitiesByEntityType(
            @PathVariable @Parameter(description = "Type d'entité") ActivityLog.EntityType entityType,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByEntityType(entityType, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/entity/{entityType}/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités pour une entité spécifique")
    public ResponseEntity<Page<ActivityLog>> getActivitiesByEntity(
            @PathVariable @Parameter(description = "Type d'entité") ActivityLog.EntityType entityType,
            @PathVariable @Parameter(description = "ID de l'entité") Long entityId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByEntity(entityType, entityId, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/action/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités par type d'action")
    public ResponseEntity<Page<ActivityLog>> getActivitiesByAction(
            @PathVariable @Parameter(description = "Type d'action") ActivityLog.Action action,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByAction(action, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités dans une plage de dates")
    public ResponseEntity<Page<ActivityLog>> getActivitiesInDateRange(
            @RequestParam @Parameter(description = "Date de début") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @Parameter(description = "Date de fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesInDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher dans les descriptions d'activités")
    public ResponseEntity<List<ActivityLog>> searchActivitiesByDescription(
            @RequestParam @Parameter(description = "Mot-clé de recherche") String keyword,
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        List<ActivityLog> activities = activityLogService.searchActivitiesByDescription(keyword, hours);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir une activité par ID")
    public ResponseEntity<ActivityLog> getActivityById(
            @PathVariable @Parameter(description = "ID de l'activité") Long id) {
        
        ActivityLog activity = activityLogService.getActivityById(id);
        return ResponseEntity.ok(activity);
    }

    // =================== STATISTIQUES ===================

    @GetMapping(value = "/stats/count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir le nombre total d'activités")
    public ResponseEntity<Map<String, Long>> getActivitiesCount(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures pour les statistiques récentes") int hours) {
        
        Long totalCount = activityLogService.getTotalActivitiesCount();
        Long recentCount = activityLogService.getRecentActivitiesCount(hours);
        
        Map<String, Long> stats = Map.of(
            "totalActivities", totalCount,
            "recentActivities", recentCount,
            "hoursRange", (long) hours
        );
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping(value = "/stats/entity-types", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les statistiques par type d'entité")
    public ResponseEntity<Map<String, Long>> getActivitiesSummaryByEntityType(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        Map<String, Long> summary = activityLogService.getActivitiesSummaryByEntityType(hours);
        return ResponseEntity.ok(summary);
    }

    @GetMapping(value = "/stats/actions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les statistiques par type d'action")
    public ResponseEntity<Map<String, Long>> getActivitiesSummaryByAction(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        Map<String, Long> summary = activityLogService.getActivitiesSummaryByAction(hours);
        return ResponseEntity.ok(summary);
    }

    @GetMapping(value = "/stats/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les statistiques par utilisateur")
    public ResponseEntity<Map<String, Long>> getActivitiesSummaryByUser(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        Map<String, Long> summary = activityLogService.getActivitiesSummaryByUser(hours);
        return ResponseEntity.ok(summary);
    }

    @GetMapping(value = "/stats/hourly", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les statistiques par heure")
    public ResponseEntity<Map<Integer, Long>> getActivitiesSummaryByHour(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        Map<Integer, Long> summary = activityLogService.getActivitiesSummaryByHour(hours);
        return ResponseEntity.ok(summary);
    }

    @GetMapping(value = "/stats/user/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les statistiques d'un utilisateur")
    public ResponseEntity<Map<String, Long>> getUserActivitiesStats(
            @PathVariable @Parameter(description = "Email de l'utilisateur") String userEmail,
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        Long userActivitiesCount = activityLogService.getUserActivitiesCount(userEmail, hours);
        
        Map<String, Long> stats = Map.of(
            "userActivities", userActivitiesCount,
            "hoursRange", (long) hours
        );
        
        return ResponseEntity.ok(stats);
    }

    // =================== GESTION DES ACTIVITÉS ===================

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une activité")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable @Parameter(description = "ID de l'activité") Long id) {
        
        activityLogService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cleanup")
    @Operation(summary = "Nettoyer les anciennes activités")
    public ResponseEntity<Map<String, String>> deleteOldActivities(
            @RequestParam(defaultValue = "30") @Parameter(description = "Nombre de jours") int daysOld) {
        
        activityLogService.deleteOldActivities(daysOld);
        
        Map<String, String> response = Map.of(
            "message", "Activités supprimées avec succès",
            "daysOld", String.valueOf(daysOld)
        );
        
        return ResponseEntity.ok(response);
    }

    // =================== ACTIVITÉS PAR CRUD ===================

    @GetMapping(value = "/crud/apprenants", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités CRUD des apprenants")
    public ResponseEntity<Page<ActivityLog>> getApprenantActivities(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByEntityType(ActivityLog.EntityType.APPRENANT, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/crud/formations", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités CRUD des formations")
    public ResponseEntity<Page<ActivityLog>> getFormationActivities(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByEntityType(ActivityLog.EntityType.FORMATION, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/crud/paiements", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités CRUD des paiements")
    public ResponseEntity<Page<ActivityLog>> getPaiementActivities(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByEntityType(ActivityLog.EntityType.PAIEMENT, pageable);
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/crud/inscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir les activités CRUD des inscriptions")
    public ResponseEntity<Page<ActivityLog>> getInscriptionActivities(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Taille de page") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ActivityLog> activities = activityLogService.getActivitiesByEntityType(ActivityLog.EntityType.INSCRIPTION, pageable);
        return ResponseEntity.ok(activities);
    }

    // =================== DASHBOARD ACTIVITIES ===================

    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir un résumé des activités pour le dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardActivities(
            @RequestParam(defaultValue = "24") @Parameter(description = "Nombre d'heures") int hours) {
        
        List<ActivityLog> recentActivities = activityLogService.getRecentActivities(Math.min(hours, 6)); // Limiter à 6h pour dashboard
        Map<String, Long> entityTypeStats = activityLogService.getActivitiesSummaryByEntityType(hours);
        Map<String, Long> actionStats = activityLogService.getActivitiesSummaryByAction(hours);
        Long totalRecentCount = activityLogService.getRecentActivitiesCount(hours);
        
        Map<String, Object> dashboardData = Map.of(
            "recentActivities", recentActivities.stream().limit(10).toList(), // Top 10
            "entityTypeStats", entityTypeStats,
            "actionStats", actionStats,
            "totalRecentCount", totalRecentCount,
            "hoursRange", hours
        );
        
        return ResponseEntity.ok(dashboardData);
    }
}
