package com.example.G_apprenant.aspect;

import com.example.G_apprenant.entity.ActivityLog;
import com.example.G_apprenant.service.ActivityLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class ActivityLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogAspect.class);

    private final ActivityLogService activityLogService;

    public ActivityLogAspect(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    // Pointcut pour capturer les méthodes des contrôleurs CRUD
    @Pointcut("execution(* com.example.G_apprenant.controller.ApprenantController.*(..))")
    public void apprenantControllerMethods() {}

    @Pointcut("execution(* com.example.G_apprenant.controller.FormationController.*(..))")
    public void formationControllerMethods() {}

    @Pointcut("execution(* com.example.G_apprenant.controller.PaiementController.*(..))")
    public void paiementControllerMethods() {}

    @Pointcut("execution(* com.example.G_apprenant.controller.InscriptionController.*(..))")
    public void inscriptionControllerMethods() {}

    @Pointcut("apprenantControllerMethods() || formationControllerMethods() || paiementControllerMethods() || inscriptionControllerMethods()")
    public void crudControllerMethods() {}

    @AfterReturning(pointcut = "crudControllerMethods()", returning = "result")
    public void logCrudActivity(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String controllerName = joinPoint.getTarget().getClass().getSimpleName();
            Object[] args = joinPoint.getArgs();

            // Déterminer le type d'entité basé sur le contrôleur
            ActivityLog.EntityType entityType = getEntityTypeFromController(controllerName);
            if (entityType == null) {
                return; // Ignore si on ne peut pas déterminer le type
            }

            // Déterminer l'action basé sur le nom de la méthode
            ActivityLog.Action action = getActionFromMethodName(methodName);
            if (action == null) {
                return; // Ignore si on ne peut pas déterminer l'action
            }

            // Extraire l'ID de l'entité si disponible
            Long entityId = extractEntityId(args, result);

            // Obtenir les informations de l'utilisateur (simulé pour l'instant)
            String userEmail = getCurrentUserEmail();
            String userName = getCurrentUserName();

            // Créer une description de l'activité
            String description = createActivityDescription(entityType, action, methodName, entityId);

            // Logger l'activité
            if (entityId != null && entityId > 0) {
                activityLogService.logActivity(entityType, action, entityId, description, userEmail, userName);
            } else {
                // Pour les actions sans ID spécifique (comme les recherches)
                activityLogService.logActivity(entityType, action, 0L, description, userEmail, userName);
            }

            logger.debug("Activité loggée: {} {} {} par {}", action, entityType, entityId, userEmail);

        } catch (Exception e) {
            logger.error("Erreur lors du logging de l'activité", e);
        }
    }

    private ActivityLog.EntityType getEntityTypeFromController(String controllerName) {
        return switch (controllerName) {
            case "ApprenantController" -> ActivityLog.EntityType.APPRENANT;
            case "FormationController" -> ActivityLog.EntityType.FORMATION;
            case "PaiementController" -> ActivityLog.EntityType.PAIEMENT;
            case "InscriptionController" -> ActivityLog.EntityType.INSCRIPTION;
            default -> null;
        };
    }

    private ActivityLog.Action getActionFromMethodName(String methodName) {
        return switch (methodName.toLowerCase()) {
            case "create", "createapprenant", "createformation", "createpaiement", "createinscription" -> ActivityLog.Action.CREATE;
            case "getbyid", "getall", "findbyid", "findall" -> ActivityLog.Action.READ;
            case "update", "updateapprenant", "updateformation", "updatepaiement", "updateinscription" -> ActivityLog.Action.UPDATE;
            case "delete", "deleteapprenant", "deleteformation", "deletepaiement", "deleteinscription" -> ActivityLog.Action.DELETE;
            case "search", "findbynom", "findbyemail", "findbyprenom" -> ActivityLog.Action.SEARCH;
            default -> {
                // Essayer de détecter par préfixe
                if (methodName.startsWith("create") || methodName.startsWith("add")) {
                    yield ActivityLog.Action.CREATE;
                } else if (methodName.startsWith("get") || methodName.startsWith("find") || methodName.startsWith("list")) {
                    yield ActivityLog.Action.READ;
                } else if (methodName.startsWith("update") || methodName.startsWith("modify") || methodName.startsWith("edit")) {
                    yield ActivityLog.Action.UPDATE;
                } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
                    yield ActivityLog.Action.DELETE;
                } else if (methodName.contains("search") || methodName.contains("find")) {
                    yield ActivityLog.Action.SEARCH;
                } else {
                    yield ActivityLog.Action.READ; // Par défaut
                }
            }
        };
    }

    private Long extractEntityId(Object[] args, Object result) {
        // Essayer d'extraire l'ID depuis les paramètres
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
            if (arg instanceof String) {
                try {
                    return Long.parseLong((String) arg);
                } catch (NumberFormatException ignored) {
                    // Ignore
                }
            }
        }

        // Essayer d'extraire l'ID depuis le résultat
        if (result instanceof ResponseEntity<?>) {
            Object body = ((ResponseEntity<?>) result).getBody();
            if (body != null) {
                return extractIdFromEntity(body);
            }
        } else if (result != null) {
            return extractIdFromEntity(result);
        }

        return 0L; // ID par défaut si non trouvé
    }

    private Long extractIdFromEntity(Object entity) {
        try {
            // Utiliser la réflection pour trouver un champ ID
            String[] idFieldNames = {"idApprenant", "idFormation", "idPaiement", "idInscription", "id"};
            
            for (String fieldName : idFieldNames) {
                try {
                    Method getter = entity.getClass().getMethod("get" + capitalize(fieldName));
                    Object value = getter.invoke(entity);
                    if (value instanceof Long) {
                        return (Long) value;
                    }
                } catch (Exception ignored) {
                    // Continue avec le prochain nom de champ
                }
            }
        } catch (Exception e) {
            logger.debug("Impossible d'extraire l'ID de l'entité: {}", e.getMessage());
        }
        return 0L;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String createActivityDescription(ActivityLog.EntityType entityType, ActivityLog.Action action, 
                                           String methodName, Long entityId) {
        String actionText = action.getDisplayName();
        String entityText = entityType.getDisplayName();
        
        if (entityId != null && entityId > 0) {
            return String.format("%s de %s #%d via %s", actionText, entityText, entityId, methodName);
        } else {
            return String.format("%s dans %s via %s", actionText, entityText, methodName);
        }
    }

    private String getCurrentUserEmail() {
        // Essayer de récupérer l'email depuis la session/sécurité
        // Pour l'instant, utiliser une valeur par défaut
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            
            // Essayer de récupérer l'utilisateur depuis le header Authorization ou session
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // Décoder le JWT pour obtenir l'email (implémentation simplifiée)
                return "user@example.com"; // TODO: Implémenter la décodage JWT
            }
            
            // Essayer depuis les paramètres de la requête ou session
            String userEmail = request.getParameter("userEmail");
            if (userEmail != null) {
                return userEmail;
            }
            
            return "system@formation.com"; // Utilisateur par défaut
        } catch (Exception e) {
            logger.debug("Impossible de récupérer l'email utilisateur: {}", e.getMessage());
            return "anonymous@formation.com";
        }
    }

    private String getCurrentUserName() {
        // Retourner un nom basé sur l'email pour l'instant
        String email = getCurrentUserEmail();
        if (email.contains("@")) {
            return email.substring(0, email.indexOf("@"));
        }
        return email;
    }
}
