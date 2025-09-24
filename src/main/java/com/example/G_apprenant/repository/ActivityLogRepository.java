package com.example.G_apprenant.repository;

import com.example.G_apprenant.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>, JpaSpecificationExecutor<ActivityLog> {

    // Recherches par type d'entité
    List<ActivityLog> findByEntityType(ActivityLog.EntityType entityType);
    Page<ActivityLog> findByEntityType(ActivityLog.EntityType entityType, Pageable pageable);
    
    // Recherches par action
    List<ActivityLog> findByAction(ActivityLog.Action action);
    Page<ActivityLog> findByAction(ActivityLog.Action action, Pageable pageable);
    
    // Recherches par utilisateur
    List<ActivityLog> findByUserEmail(String userEmail);
    Page<ActivityLog> findByUserEmail(String userEmail, Pageable pageable);
    
    // Recherches par entité spécifique
    List<ActivityLog> findByEntityTypeAndEntityId(ActivityLog.EntityType entityType, Long entityId);
    Page<ActivityLog> findByEntityTypeAndEntityId(ActivityLog.EntityType entityType, Long entityId, Pageable pageable);
    
    // Recherches par période
    List<ActivityLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    Page<ActivityLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Recherches récentes
    List<ActivityLog> findByTimestampAfter(LocalDateTime since);
    Page<ActivityLog> findByTimestampAfter(LocalDateTime since, Pageable pageable);
    
    // Activités récentes par utilisateur
    List<ActivityLog> findByUserEmailAndTimestampAfter(String userEmail, LocalDateTime since);
    Page<ActivityLog> findByUserEmailAndTimestampAfter(String userEmail, LocalDateTime since, Pageable pageable);
    
    // Activités récentes par type d'entité
    List<ActivityLog> findByEntityTypeAndTimestampAfter(ActivityLog.EntityType entityType, LocalDateTime since);
    Page<ActivityLog> findByEntityTypeAndTimestampAfter(ActivityLog.EntityType entityType, LocalDateTime since, Pageable pageable);
    
    // Requêtes personnalisées avec JPQL
    @Query("SELECT a FROM ActivityLog a WHERE a.timestamp >= :since ORDER BY a.timestamp DESC")
    List<ActivityLog> findRecentActivities(@Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.timestamp >= :since ORDER BY a.timestamp DESC")
    Page<ActivityLog> findRecentActivities(@Param("since") LocalDateTime since, Pageable pageable);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.userEmail = :userEmail AND a.timestamp >= :since ORDER BY a.timestamp DESC")
    List<ActivityLog> findUserRecentActivities(@Param("userEmail") String userEmail, @Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM ActivityLog a WHERE a.entityType = :entityType AND a.action = :action AND a.timestamp >= :since ORDER BY a.timestamp DESC")
    List<ActivityLog> findActivitiesByTypeAndAction(@Param("entityType") ActivityLog.EntityType entityType, 
                                                   @Param("action") ActivityLog.Action action, 
                                                   @Param("since") LocalDateTime since);
    
    // Statistiques
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.timestamp >= :since")
    Long countActivitiesSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.entityType = :entityType AND a.timestamp >= :since")
    Long countActivitiesByTypeAndSince(@Param("entityType") ActivityLog.EntityType entityType, @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.action = :action AND a.timestamp >= :since")
    Long countActivitiesByActionAndSince(@Param("action") ActivityLog.Action action, @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.userEmail = :userEmail AND a.timestamp >= :since")
    Long countUserActivitiesSince(@Param("userEmail") String userEmail, @Param("since") LocalDateTime since);
    
    // Top activités par type
    @Query("SELECT a.entityType, COUNT(a) as count FROM ActivityLog a WHERE a.timestamp >= :since GROUP BY a.entityType ORDER BY count DESC")
    List<Object[]> getTopEntityTypesByActivity(@Param("since") LocalDateTime since);
    
    @Query("SELECT a.action, COUNT(a) as count FROM ActivityLog a WHERE a.timestamp >= :since GROUP BY a.action ORDER BY count DESC")
    List<Object[]> getTopActionsByActivity(@Param("since") LocalDateTime since);
    
    @Query("SELECT a.userEmail, COUNT(a) as count FROM ActivityLog a WHERE a.timestamp >= :since GROUP BY a.userEmail ORDER BY count DESC")
    List<Object[]> getTopUsersByActivity(@Param("since") LocalDateTime since);
    
    // Recherche par description
    @Query("SELECT a FROM ActivityLog a WHERE LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')) AND a.timestamp >= :since ORDER BY a.timestamp DESC")
    List<ActivityLog> searchByDescription(@Param("keyword") String keyword, @Param("since") LocalDateTime since);
    
    // Activités par plage horaire
    @Query("SELECT HOUR(a.timestamp) as hour, COUNT(a) as count FROM ActivityLog a WHERE a.timestamp >= :since GROUP BY HOUR(a.timestamp) ORDER BY hour")
    List<Object[]> getActivitiesByHour(@Param("since") LocalDateTime since);
    
    // Nettoyage des anciennes activités
    @Query("DELETE FROM ActivityLog a WHERE a.timestamp < :beforeDate")
    void deleteActivitiesOlderThan(@Param("beforeDate") LocalDateTime beforeDate);
}
