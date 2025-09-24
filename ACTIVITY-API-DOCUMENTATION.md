# API de Gestion des Activités Récentes

## Vue d'ensemble

Cette API permet de tracker et consulter les activités récentes sur les opérations CRUD effectuées dans l'application de gestion de formation. Elle fournit des endpoints pour :

- ✅ **Consulter les activités récentes** par type, utilisateur, période
- ✅ **Obtenir des statistiques** d'utilisation et d'activité
- ✅ **Filtrer les activités** par entité (Apprenants, Formations, Paiements, Inscriptions)
- ✅ **Logging automatique** des actions CRUD via AOP

## Endpoints Principaux

### 📊 Consultation des Activités

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/activities` | GET | Toutes les activités (paginées) |
| `/api/activities/recent` | GET | Activités récentes (par heures) |
| `/api/activities/user/{email}` | GET | Activités d'un utilisateur |
| `/api/activities/entity/{type}` | GET | Activités par type d'entité |
| `/api/activities/action/{action}` | GET | Activités par type d'action |
| `/api/activities/{id}` | GET | Une activité spécifique |

### 📈 Statistiques

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/activities/stats/count` | GET | Nombre total d'activités |
| `/api/activities/stats/entity-types` | GET | Répartition par type d'entité |
| `/api/activities/stats/actions` | GET | Répartition par type d'action |
| `/api/activities/stats/users` | GET | Activité par utilisateur |
| `/api/activities/stats/hourly` | GET | Répartition par heure |

### 🎯 Activités CRUD Spécialisées

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/activities/crud/apprenants` | GET | Activités CRUD des apprenants |
| `/api/activities/crud/formations` | GET | Activités CRUD des formations |
| `/api/activities/crud/paiements` | GET | Activités CRUD des paiements |
| `/api/activities/crud/inscriptions` | GET | Activités CRUD des inscriptions |

### 🏠 Dashboard

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/activities/dashboard` | GET | Résumé pour dashboard |

## Structure des Données

### Entité ActivityLog

```json
{
  "idActivity": 1,
  "entityType": "APPRENANT",
  "action": "CREATE",
  "entityId": 123,
  "description": "Création de Apprenant #123 via create",
  "userEmail": "admin@formation.com",
  "userName": "admin",
  "timestamp": "2024-09-24T14:30:00",
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0...",
  "oldValues": null,
  "newValues": "{\"nom\":\"Dupont\"...}"
}
```

### Types d'Entités

- `APPRENANT` - Gestion des apprenants
- `FORMATION` - Gestion des formations
- `PAIEMENT` - Gestion des paiements
- `INSCRIPTION` - Gestion des inscriptions
- `UTILISATEUR` - Gestion des utilisateurs

### Types d'Actions

- `CREATE` - Création d'un enregistrement
- `READ` - Consultation d'un enregistrement
- `UPDATE` - Modification d'un enregistrement
- `DELETE` - Suppression d'un enregistrement
- `SEARCH` - Recherche dans les données
- `LOGIN` - Connexion utilisateur
- `LOGOUT` - Déconnexion utilisateur

## Exemples d'Utilisation

### 1. Obtenir les activités récentes (dernières 24h)

```bash
GET /api/activities/recent?hours=24

Response:
[
  {
    "idActivity": 15,
    "entityType": "APPRENANT",
    "action": "CREATE",
    "entityId": 45,
    "description": "Création de Apprenant #45 via create",
    "userEmail": "admin@formation.com",
    "userName": "admin",
    "timestamp": "2024-09-24T14:25:00"
  }
]
```

### 2. Statistiques par type d'entité

```bash
GET /api/activities/stats/entity-types?hours=24

Response:
{
  "Apprenant": 15,
  "Formation": 8,
  "Inscription": 12,
  "Paiement": 5
}
```

### 3. Dashboard des activités

```bash
GET /api/activities/dashboard?hours=6

Response:
{
  "recentActivities": [...], // 10 dernières activités
  "entityTypeStats": {...},  // Stats par entité
  "actionStats": {...},      // Stats par action
  "totalRecentCount": 25,
  "hoursRange": 6
}
```

### 4. Activités d'un utilisateur spécifique

```bash
GET /api/activities/user/admin@formation.com?page=0&size=10

Response:
{
  "content": [...],
  "totalElements": 45,
  "totalPages": 5,
  "number": 0,
  "size": 10
}
```

## Logging Automatique

### Comment ça fonctionne

L'API utilise **Spring AOP** pour automatiquement capturer les activités CRUD :

1. **Pointcuts** définis sur les contrôleurs principaux
2. **Interception** des méthodes après exécution réussie
3. **Extraction** automatique des informations (type, action, ID)
4. **Enregistrement** en base de données

### Contrôleurs Surveillés

- `ApprenantController` → `EntityType.APPRENANT`
- `FormationController` → `EntityType.FORMATION`
- `PaiementController` → `EntityType.PAIEMENT`
- `InscriptionController` → `EntityType.INSCRIPTION`

### Actions Détectées Automatiquement

- Méthodes commençant par `create*` → `Action.CREATE`
- Méthodes commençant par `get*`, `find*` → `Action.READ`
- Méthodes commençant par `update*` → `Action.UPDATE`
- Méthodes commençant par `delete*` → `Action.DELETE`
- Méthodes contenant `search` → `Action.SEARCH`

## Configuration

### Base de Données

La table `activity_log` est créée automatiquement avec Hibernate DDL :

```sql
CREATE TABLE activity_log (
    id_activity BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    action VARCHAR(20) NOT NULL,
    entity_id BIGINT NOT NULL,
    description VARCHAR(200),
    user_email VARCHAR(100) NOT NULL,
    user_name VARCHAR(100),
    timestamp TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    old_values TEXT,
    new_values TEXT
);
```

### Index Automatiques

- `idx_activity_log_timestamp` - Tri par date
- `idx_activity_log_entity_type` - Filtrage par entité
- `idx_activity_log_action` - Filtrage par action
- `idx_activity_log_user_email` - Filtrage par utilisateur

## Paramètres Communs

### Pagination

- `page` (défaut: 0) - Numéro de page
- `size` (défaut: 20) - Taille de page
- `sortBy` (défaut: "timestamp") - Champ de tri
- `sortDir` (défaut: "desc") - Direction du tri

### Filtrage Temporel

- `hours` (défaut: 24) - Nombre d'heures dans le passé
- `startDate` - Date de début (format ISO)
- `endDate` - Date de fin (format ISO)

## Tests

### Script de Test Automatisé

Un script `test-activities-api.sh` est fourni pour tester tous les endpoints :

```bash
./test-activities-api.sh
```

### Prérequis pour les Tests

1. Application Spring Boot démarrée :
   ```bash
   ./mvnw spring-boot:run
   ```

2. `curl` et `jq` installés pour les tests

### Génération de Données Test

Pour générer des activités de test, utilisez les endpoints CRUD existants :

```bash
# Créer un apprenant (génère une activité CREATE)
curl -X POST http://localhost:8080/api/apprenants \
  -H "Content-Type: application/json" \
  -d '{"nom":"Dupont","prenom":"Jean","email":"jean@test.com","cin":"123456789"}'

# Consulter les apprenants (génère une activité READ)
curl http://localhost:8080/api/apprenants

# Les activités apparaîtront automatiquement dans /api/activities
```

## Documentation Interactive

La documentation complète est disponible via **Swagger UI** :

🌐 **http://localhost:8080/api/swagger-ui.html**

## Maintenance

### Nettoyage des Anciennes Activités

```bash
DELETE /api/activities/cleanup?daysOld=30
```

Supprime les activités plus anciennes que le nombre de jours spécifié.

### Performance

- Les requêtes utilisent des **index optimisés**
- **Pagination** obligatoire pour les gros volumes
- **Cache** possible via Spring Cache
- **Archivage** recommandé pour les données anciennes

## Sécurité

- [ ] **Authentification** JWT (à implémenter)
- [ ] **Autorisation** par rôle (à implémenter)
- [x] **Validation** des paramètres d'entrée
- [x] **Protection** contre l'injection SQL (JPA/Hibernate)
- [ ] **Audit trail** complet avec IP et User-Agent

---

## 🚀 Démarrage Rapide

1. **Compiler** le projet :
   ```bash
   ./mvnw clean compile
   ```

2. **Démarrer** l'application :
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Tester** l'API :
   ```bash
   ./test-activities-api.sh
   ```

4. **Explorer** via Swagger :
   ```
   http://localhost:8080/api/swagger-ui.html
   ```

L'API des activités récentes est maintenant **opérationnelle** ! 🎉
