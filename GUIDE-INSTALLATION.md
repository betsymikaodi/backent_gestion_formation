# 📋 Guide d'Installation et Utilisation
## Système de Gestion des Apprenants et Formations

Ce guide vous explique comment utiliser le système complet qui a été installé et configuré.

## ✅ **RÉCAPITULATIF DE L'INSTALLATION**

### 🗃️ Base de Données PostgreSQL
- **✅ PostgreSQL 16 installé** sur Ubuntu
- **✅ Base de données créée** : `centre_formation`
- **✅ Utilisateur** : `postgres` / **Mot de passe** : `postgres`
- **✅ 4 Tables créées** avec index optimisés :
  - `apprenant` (5 enregistrements de test)
  - `formation` (5 formations de test)
  - `inscription` (6 inscriptions de test)
  - `paiement` (11 paiements de test)

### 🚀 Application Spring Boot
- **✅ Backend complet** avec API REST
- **✅ Architecture ORM pure** (sans SQL dans le code)
- **✅ Documentation Swagger** automatique
- **✅ Validation des données** complète
- **✅ Gestion d'erreurs** professionnelle

## 🚀 **DÉMARRER L'APPLICATION**

### Option 1 : Script automatique (recommandé)
```bash
cd /home/fandresena/Documents/projet/G_apprenant
./start-app.sh
```

### Option 2 : Démarrage manuel
```bash
# 1. Vérifier PostgreSQL
sudo systemctl status postgresql

# 2. Démarrer PostgreSQL si nécessaire
sudo systemctl start postgresql

# 3. Aller dans le dossier du projet
cd /home/fandresena/Documents/projet/G_apprenant

# 4. Démarrer l'application
./mvnw spring-boot:run
```

## 📚 **ACCÈS AUX SERVICES**

Une fois l'application démarrée, vous pouvez accéder à :

### 🌐 API REST
- **URL de base** : http://localhost:8080/api
- **Format** : JSON
- **CORS** : Configuré pour les frontends

### 📖 Documentation Interactive (Swagger)
- **URL** : http://localhost:8080/api/swagger-ui.html
- **Fonctionnalités** :
  - Interface graphique pour tester l'API
  - Documentation complète de tous les endpoints
  - Possibilité d'exécuter des requêtes directement

### 🔍 Monitoring (Actuator)
- **Santé de l'application** : http://localhost:8080/api/actuator/health
- **Informations** : http://localhost:8080/api/actuator/info
- **Métriques** : http://localhost:8080/api/actuator/metrics

## 🎯 **ENDPOINTS PRINCIPAUX**

### 👥 Gestion des Apprenants
```http
GET    /api/apprenants              # Liste tous les apprenants
POST   /api/apprenants              # Créer un nouvel apprenant
GET    /api/apprenants/{id}         # Détails d'un apprenant
PUT    /api/apprenants/{id}         # Modifier un apprenant
DELETE /api/apprenants/{id}         # Supprimer un apprenant

# Recherches
GET    /api/apprenants/search/nom?nom=RAKOTO
GET    /api/apprenants/paginated?page=0&size=10
```

### 📚 Gestion des Formations
```http
GET    /api/formations                    # Liste des formations
POST   /api/formations                    # Créer une formation
GET    /api/formations/{id}               # Détails d'une formation
PUT    /api/formations/{id}               # Modifier une formation
DELETE /api/formations/{id}               # Supprimer une formation

# Recherches et statistiques
GET    /api/formations/search/prix?fraisMin=100000&fraisMax=500000
GET    /api/formations/populaires
GET    /api/formations/stats/moyenne-prix
```

### 📝 Gestion des Inscriptions
```http
GET    /api/inscriptions           # Liste des inscriptions
POST   /api/inscriptions          # Nouvelle inscription
GET    /api/inscriptions/{id}     # Détails d'une inscription
PUT    /api/inscriptions/{id}     # Modifier une inscription
```

### 💰 Gestion des Paiements
```http
GET    /api/paiements             # Liste des paiements
POST   /api/paiements            # Nouveau paiement
GET    /api/paiements/{id}       # Détails d'un paiement
```

## 🧪 **TESTER L'API**

### 1. Avec Swagger UI (recommandé)
- Ouvrez http://localhost:8080/api/swagger-ui.html
- Sélectionnez un endpoint
- Cliquez sur "Try it out"
- Modifiez les paramètres si nécessaire
- Cliquez sur "Execute"

### 2. Avec cURL (ligne de commande)
```bash
# Obtenir tous les apprenants
curl -X GET http://localhost:8080/api/apprenants

# Obtenir toutes les formations
curl -X GET http://localhost:8080/api/formations

# Créer un nouvel apprenant
curl -X POST http://localhost:8080/api/apprenants \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "NOUVEAU",
    "prenom": "Test", 
    "email": "test@email.com",
    "cin": "999888777666",
    "telephone": "+261 34 99 88 77"
  }'
```

### 3. Avec un client REST (Postman, Insomnia, etc.)
- URL de base : `http://localhost:8080/api`
- Headers : `Content-Type: application/json`
- Utilisez les endpoints documentés ci-dessus

## 🗄️ **ACCÈS DIRECT À LA BASE DE DONNÉES**

### Se connecter à PostgreSQL
```bash
sudo -u postgres psql -d centre_formation
```

### Requêtes utiles
```sql
-- Voir tous les apprenants
SELECT * FROM apprenant;

-- Voir toutes les formations
SELECT * FROM formation;

-- Voir les inscriptions avec détails
SELECT 
    a.nom, a.prenom,
    f.nom as formation,
    i.statut, i.date_inscription
FROM apprenant a
JOIN inscription i ON a.id_apprenant = i.id_apprenant
JOIN formation f ON i.id_formation = f.id_formation
ORDER BY a.nom;

-- Voir les paiements
SELECT 
    a.nom, a.prenom,
    p.montant, p.mode_paiement, p.date_paiement
FROM apprenant a
JOIN inscription i ON a.id_apprenant = i.id_apprenant
JOIN paiement p ON i.id_inscription = p.id_inscription
ORDER BY p.date_paiement DESC;
```

## 🔧 **DONNÉES DE TEST DISPONIBLES**

Le système contient des données de démonstration :

### 👥 Apprenants (5)
- Jean RAKOTO - jean.rakoto@email.com
- Marie RABE - marie.rabe@email.com  
- Paul ANDRY - paul.andry@email.com
- Sophie HERY - sophie.hery@email.com
- David RINA - david.rina@email.com

### 📚 Formations (5)
- Développement Web Full Stack (120h - 500,000 Ar)
- Python pour Data Science (80h - 350,000 Ar)
- Marketing Digital (60h - 250,000 Ar)
- Comptabilité et Gestion (100h - 300,000 Ar)
- Anglais Commercial (40h - 150,000 Ar)

### 📊 Statistiques des données de test
- **6 inscriptions** avec différents statuts
- **11 paiements** avec différents modes
- **Relations complètes** entre toutes les entités

## 🚨 **DÉPANNAGE**

### PostgreSQL ne démarre pas
```bash
sudo systemctl status postgresql
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Erreur de connexion à la base
- Vérifiez que PostgreSQL fonctionne
- Vérifiez les credentials dans `application.properties`
- Vérifiez que la base `centre_formation` existe

### Port 8080 déjà utilisé
```bash
# Trouver le processus utilisant le port
sudo lsof -i :8080

# Arrêter l'application si elle tourne déjà
pkill -f "spring-boot"

# Ou modifier le port dans application.properties
server.port=8081
```

### Problèmes de compilation Maven
```bash
# Nettoyer et recompiler
./mvnw clean compile

# Vérifier la version Java
java -version
```

## 📁 **STRUCTURE DU PROJET**

```
G_apprenant/
├── src/main/java/com/example/G_apprenant/
│   ├── config/             # Configuration CORS
│   ├── controller/         # API REST Controllers
│   ├── dto/               # Data Transfer Objects  
│   ├── entity/            # Entités JPA
│   ├── exception/         # Gestion d'erreurs
│   ├── repository/        # Repositories ORM
│   ├── service/           # Logique métier
│   └── specification/     # Filtres avancés
├── src/main/resources/
│   ├── application.properties  # Configuration
│   └── data-sample.sql        # Données de test
├── create-tables.sql          # Script de création DB
├── start-app.sh              # Script de démarrage
├── README.md                 # Documentation complète
└── GUIDE-INSTALLATION.md     # Ce guide
```

## 🎓 **PROCHAINES ÉTAPES**

Une fois que vous maîtrisez le système de base, vous pouvez :

1. **Développer un frontend** (React, Angular, Vue.js)
2. **Ajouter l'authentification** (Spring Security)
3. **Implémenter les notifications** (email, SMS)
4. **Créer des rapports** (PDF, Excel)
5. **Déployer en production** (Docker, Cloud)

## 📞 **SUPPORT**

- **Documentation complète** : Voir README.md
- **API Documentation** : http://localhost:8080/api/swagger-ui.html
- **Logs de l'application** : Vérifiez la console lors du démarrage

---

**🎉 Félicitations ! Votre système de gestion des apprenants est opérationnel !** 🚀