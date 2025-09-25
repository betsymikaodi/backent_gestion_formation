# API Reference - Backend Gestion Formation

Base URL par défaut: `http://localhost:8080/api`

Note: en développement, vous pouvez lancer l'app sur un autre port (ex: 8081). Adaptez la base URL en conséquence.

---

## Authentification (/auth)

- POST /auth/register
  - Crée un utilisateur.
  - Body (json): { nom, prenom, email, motDePasse, role? }

```bash
# Exemple
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Admin","prenom":"Root","email":"admin@example.com","motDePasse":"secret","role":"ADMIN"}'
```

- POST /auth/login
  - Authentifie et retourne un token JWT.
  - Body (json): { email, password }

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","password":"secret"}'
```

- GET /auth/me
```bash
curl -H 'Authorization: Bearer {{JWT}}' http://localhost:8080/api/auth/me
```

---

## Apprenants (/apprenants)

- POST /apprenants
  - Crée un apprenant.

```bash
curl -X POST http://localhost:8080/api/apprenants \
  -H 'Content-Type: application/json' \
  -d '{
    "nom":"Dupont","prenom":"Jean","email":"jean.dupont@example.com",
    "telephone":"0341234567","adresse":"Antananarivo",
    "cin":"CIN123456","dateNaissance":"1995-05-15"
  }'
```

- GET /apprenants (pagination/tri pro)
  - Query params: page, size (10,20,50,100), sortBy (nom,prenom,email,telephone,dateNaissance,dateNow), sortDirection (asc|desc), search, nom, prenom, email, telephone, adresse, cin, dateNaissanceDebut, dateNaissanceFin, statutInscription, formationId, nomFormation.

```bash
# Page 0, 20 items, tri récents d'abord (défaut)
curl http://localhost:8080/api/apprenants\?page\=0\&size\=20

# Tri par nom asc + recherche globale
curl 'http://localhost:8080/api/apprenants?sortBy=nom&sortDirection=asc&search=martin&page=0&size=20'
```

- GET /apprenants/all
- GET /apprenants/{id}
- PUT /apprenants/{id}
- DELETE /apprenants/{id}
- GET /apprenants/search/nom?nom=...
- GET /apprenants/search/email?email=...
- GET /apprenants/count
- GET /apprenants/sizes
- GET /apprenants/sort-options

---

## Formations (/formations)

- POST /formations
- GET /formations
- GET /formations/{id}
- PUT /formations/{id}
- DELETE /formations/{id}

```bash
curl http://localhost:8080/api/formations
```

---

## Inscriptions (/inscriptions)

- POST /inscriptions
- POST /inscriptions/enroll (alias)
- GET /inscriptions
- GET /inscriptions/{id}
- PUT /inscriptions/{id}/confirm
- PUT /inscriptions/{id}/cancel
- PUT /inscriptions/{id} (maj droitInscription/statut)
- DELETE /inscriptions/{id}

```bash
# Créer une inscription
curl -X POST http://localhost:8080/api/inscriptions \
  -H 'Content-Type: application/json' \
  -d '{"apprenantId":1,"formationId":1,"droitInscription":50000}'
```

---

## Paiements (/paiements)

- POST /paiements
- GET /paiements
- GET /paiements/{id}
- GET /paiements/inscription/{id}
- PUT /paiements/{id}
- DELETE /paiements/{id}

```bash
# Créer un paiement
curl -X POST http://localhost:8080/api/paiements \
  -H 'Content-Type: application/json' \
  -d '{"inscriptionId":1,"montant":130000,"modePaiement":"Virement","module":"Module 1"}'
```

---

## Utilisateurs (/utilisateurs)

- GET /utilisateurs
- GET /utilisateurs/{id}
- POST /utilisateurs
- PUT /utilisateurs/{id}
- DELETE /utilisateurs/{id}
- Endpoints admin/stats (nécessitent ROLE_ADMIN):
  - /utilisateurs/admin/**
  - /utilisateurs/stats/**

---

## Journaux d’activité (/activities)

- GET /activities
- GET /activities/{id}
- GET /activities/stats/**
- GET /activities/dashboard/**
- GET /activities/crud/**
- DELETE /activities/** (ROLE_ADMIN)

---

## Statistiques (Dashboard) (/stats)

- GET /stats/overview
  - KPIs: Revenus totaux (+%), Total inscriptions (+%), Revenu moyen/étudiant (+%).
  - Params: period=day|week|month|custom (défaut: month), startDate, endDate (si custom, format yyyy-MM-dd)

```bash
# Mois courant (défaut)
curl http://localhost:8080/api/stats/overview

# Semaine courante
curl 'http://localhost:8080/api/stats/overview?period=week'

# Période personnalisée
curl 'http://localhost:8080/api/stats/overview?period=custom&startDate=2025-09-01&endDate=2025-09-15'
```

Exemple de réponse:
```json
{
  "totalRevenue": {
    "label": "Revenus totaux",
    "value": 130000.00,
    "previousValue": 0.00,
    "percentageChange": 100.0,
    "periodStart": "2025-09-01",
    "periodEnd": "2025-09-30",
    "previousPeriodStart": "2025-08-02",
    "previousPeriodEnd": "2025-08-31",
    "unit": "€"
  },
  "totalEnrollments": {
    "label": "Total inscriptions",
    "value": 1,
    "previousValue": 0,
    "percentageChange": 100.0,
    "periodStart": "2025-09-01",
    "periodEnd": "2025-09-30",
    "previousPeriodStart": "2025-08-02",
    "previousPeriodEnd": "2025-08-31",
    "unit": "inscrits"
  },
  "avgRevenuePerStudent": {
    "label": "Revenu moyen par étudiant",
    "value": 130000.00,
    "previousValue": 0,
    "percentageChange": 100.0,
    "periodStart": "2025-09-01",
    "periodEnd": "2025-09-30",
    "previousPeriodStart": "2025-08-02",
    "previousPeriodEnd": "2025-08-31",
    "unit": "€/étudiant"
  }
}
```

---

## Docs & Health

- Swagger UI: GET /swagger-ui/index.html
- OpenAPI JSON: GET /v3/api-docs
- Health: GET /actuator/health

```bash
curl http://localhost:8080/api/actuator/health
```

---

## Sécurité & CORS (dev)

- Plusieurs GET sont ouverts en lecture pour faciliter le dev (voir SecurityConfig). À renforcer en prod.
- CORS autorise: http://localhost:3000, 4200, 8081 (config WebConfig).
- Pour les routes sécurisées: ajouter `Authorization: Bearer {{JWT}}`.

---

## Notes Frontend

- Contexte global `/api` à inclure dans toutes les URLs.
- Les endpoints Apprenants offrent une pagination et un tri avancés compatibles avec des listes, filtres et barres de recherche temps réel.
- Les stats de dashboard offrent des comparaisons vs période précédente.

