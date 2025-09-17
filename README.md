# 🎓 Système de Gestion des Apprenants et Formations

Un système complet de gestion des inscriptions, formations et paiements pour un centre de formation, développé avec **Spring Boot 3.5.5** et **PostgreSQL**.

## 🌟 Fonctionnalités

### Gestion des Apprenants
- ✅ Inscription des apprenants avec validation complète
- ✅ Recherche avancée (nom, prénom, email, âge, CIN)
- ✅ Historique des inscriptions et formations
- ✅ Pagination et tri des résultats

### Gestion des Formations
- ✅ CRUD complet des formations
- ✅ Recherche par nom, prix, durée
- ✅ Statistiques (formations populaires, prix moyens)
- ✅ Gestion des places disponibles

### Gestion des Inscriptions
- ✅ Inscription aux formations avec validation
- ✅ Suivi des statuts (En attente, Confirmé, Annulé)
- ✅ Calcul automatique des montants
- ✅ Historique complet

### Gestion des Paiements
- ✅ Enregistrement des paiements par modules
- ✅ Multiple modes de paiement (Espèce, Virement, Mobile Money)
- ✅ Calcul des soldes restants
- ✅ Reporting financier

## 🏗️ Architecture Technique

### Stack Technologique
- **Backend**: Spring Boot 3.5.5
- **Base de données**: PostgreSQL
- **ORM**: Hibernate/JPA (100% ORM, sans SQL brut)
- **Validation**: Bean Validation (JSR-380)
- **Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Build**: Maven

### Architecture en Couches
```
┌─────────────────────────────────────┐
│         Controllers REST            │  ← API REST avec Swagger
├─────────────────────────────────────┤
│           Services                  │  ← Logique métier
├─────────────────────────────────────┤
│         Repositories                │  ← Accès aux données (JPA)
├─────────────────────────────────────┤
│         Entities (JPA)              │  ← Modèle de données
├─────────────────────────────────────┤
│        PostgreSQL Database          │  ← Persistance
└─────────────────────────────────────┘
```

## 🗃️ Modèle de Données

### Tables Principales
1. **apprenant** - Informations des étudiants
2. **formation** - Catalogue des formations
3. **inscription** - Liens apprenant-formation
4. **paiement** - Transactions financières

### Relations
- Un apprenant peut avoir plusieurs inscriptions
- Une formation peut avoir plusieurs inscrits
- Une inscription peut avoir plusieurs paiements
- Index optimisés pour les recherches fréquentes

## 🚀 Installation et Configuration

### Prérequis
- ☑️ Java 17+
- ☑️ PostgreSQL 12+
- ☑️ Maven 3.6+ (ou utiliser ./mvnw)

### 1. Base de données
```sql
-- Créer la base de données
CREATE DATABASE centre_formation;

-- Se connecter à la base
\c centre_formation;

-- Les tables seront créées automatiquement par Hibernate
```

### 2. Configuration
Modifier `src/main/resources/application.properties` :

```properties
# Base de données
spring.datasource.url=jdbc:postgresql://localhost:5432/centre_formation
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe

# Activer les logs SQL en développement
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### 3. Lancement
```bash
# Avec Maven installé
mvn spring-boot:run

# Ou avec le wrapper Maven (recommandé)
./mvnw spring-boot:run

# Ou compiler et lancer
./mvnw clean package
java -jar target/G_apprenant-0.0.1-SNAPSHOT.jar
```

L'application sera accessible sur : http://localhost:8080/api

## 📚 Documentation API

### Swagger UI
Une fois l'application lancée, accédez à la documentation interactive :
- **Swagger UI** : http://localhost:8080/api/swagger-ui.html
- **API Docs JSON** : http://localhost:8080/api/api-docs

### Endpoints Principaux

#### 👥 Apprenants (`/api/apprenants`)
```http
GET    /api/apprenants              # Liste tous les apprenants
GET    /api/apprenants/{id}         # Détails d'un apprenant
POST   /api/apprenants              # Créer un apprenant
PUT    /api/apprenants/{id}         # Modifier un apprenant
DELETE /api/apprenants/{id}         # Supprimer un apprenant
GET    /api/apprenants/search/nom   # Recherche par nom
```

#### 📚 Formations (`/api/formations`)
```http
GET    /api/formations                    # Liste des formations
GET    /api/formations/{id}               # Détails d'une formation
POST   /api/formations                    # Créer une formation
PUT    /api/formations/{id}               # Modifier une formation
DELETE /api/formations/{id}               # Supprimer une formation
GET    /api/formations/search/prix       # Recherche par prix
GET    /api/formations/populaires        # Formations populaires
GET    /api/formations/stats/moyenne-prix # Prix moyen
```

#### 📝 Inscriptions (`/api/inscriptions`)
```http
GET    /api/inscriptions           # Liste des inscriptions
POST   /api/inscriptions          # Nouvelle inscription
GET    /api/inscriptions/{id}     # Détails d'une inscription
PUT    /api/inscriptions/{id}     # Modifier une inscription
```

#### 💰 Paiements (`/api/paiements`)
```http
GET    /api/paiements             # Liste des paiements
POST   /api/paiements            # Nouveau paiement
GET    /api/paiements/{id}       # Détails d'un paiement
```

## 🔧 Exemples d'Utilisation

### Créer un Apprenant
```json
POST /api/apprenants
{
  "nom": "RAKOTO",
  "prenom": "Jean",
  "email": "jean.rakoto@email.com",
  "cin": "123456789012",
  "telephone": "+261 34 12 345 67",
  "adresse": "Antananarivo, Madagascar",
  "dateNaissance": "1995-05-15"
}
```

### Créer une Formation
```json
POST /api/formations
{
  "nom": "Développement Web Full Stack",
  "description": "Formation complète en développement web",
  "duree": 120,
  "frais": 500000.00
}
```

### Créer une Inscription
```json
POST /api/inscriptions
{
  "apprenantId": 1,
  "formationId": 1,
  "droitInscription": 25000.00
}
```

### Enregistrer un Paiement
```json
POST /api/paiements
{
  "inscriptionId": 1,
  "montant": 150000.00,
  "modePaiement": "Mobile Money",
  "module": "Module 1"
}
```

## 🔍 Recherches Avancées

### Recherche d'Apprenants
```http
# Par nom
GET /api/apprenants/search/nom?nom=RAKOTO

# Avec pagination
GET /api/apprenants/paginated?page=0&size=10&sortBy=nom&sortDir=asc
```

### Filtrage des Formations
```http
# Par fourchette de prix
GET /api/formations/search/prix?fraisMin=100000&fraisMax=500000

# Par durée
GET /api/formations/search/duree?dureeMin=40&dureeMax=120
```

## 📊 Statistiques et Reporting

### Endpoints de Statistiques
```http
# Statistiques formations
GET /api/formations/stats/moyenne-prix      # Prix moyen
GET /api/formations/stats/moyenne-duree     # Durée moyenne
GET /api/formations/{id}/inscriptions/count # Nombre d'inscrits

# Formations spéciales
GET /api/formations/populaires              # Les plus populaires
GET /api/formations/moins-cheres            # Les moins chères
GET /api/formations/disponibles             # Avec places disponibles
```

## 🛠️ Développement

### Structure du Projet
```
src/main/java/com/example/G_apprenant/
├── config/              # Configuration (CORS, etc.)
├── controller/          # Controllers REST
├── dto/                # Data Transfer Objects
├── entity/             # Entités JPA
├── exception/          # Gestion des exceptions
├── repository/         # Repositories JPA
├── service/            # Services métier
│   └── impl/          # Implémentations
├── specification/      # Spécifications JPA
└── GApprenantApplication.java
```

### Bonnes Pratiques Appliquées
- ✅ **Architecture en couches** claire et séparée
- ✅ **Injection de dépendances** avec Spring
- ✅ **Validation** complète avec Bean Validation
- ✅ **Gestion d'erreurs** centralisée
- ✅ **Documentation API** automatique avec Swagger
- ✅ **ORM pur** sans requêtes SQL brutes
- ✅ **CORS** configuré pour les frontends
- ✅ **Logging** configuré et structuré

### Tests
```bash
# Lancer tous les tests
./mvnw test

# Tests spécifiques
./mvnw test -Dtest=ApprenantControllerTest
```

## 🌐 Frontend

### CORS Configuration
Le backend est configuré pour accepter les requêtes depuis :
- React (http://localhost:3000)
- Angular (http://localhost:4200) 
- Vue.js (http://localhost:8081)

### Exemples d'Intégration
```javascript
// Exemple avec fetch API
const response = await fetch('http://localhost:8080/api/apprenants', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(apprenantData)
});

const result = await response.json();
```

## 📋 TODO / Améliorations Futures

### Fonctionnalités
- [ ] Système d'authentification/autorisation
- [ ] Notifications email/SMS
- [ ] Export Excel/PDF des rapports
- [ ] Dashboard avec graphiques
- [ ] API de synchronisation mobile
- [ ] Système de notes et évaluations

### Technique  
- [ ] Tests d'intégration complets
- [ ] Cache Redis pour les performances
- [ ] Migration Flyway
- [ ] Monitoring avec Actuator
- [ ] Conteneurisation Docker
- [ ] CI/CD avec GitHub Actions

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commit vos changements (`git commit -m 'Ajouter nouvelle fonctionnalité'`)
4. Push la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Ouvrir une Pull Request

## 📄 License

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 📞 Support

Pour toute question ou problème :
- Ouvrir une [issue](https://github.com/votre-username/G_apprenant/issues)
- Consulter la documentation Swagger
- Vérifier les logs de l'application

---

**Développé avec ❤️ et Spring Boot** 🚀