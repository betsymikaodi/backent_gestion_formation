# 🎓 Système de Gestion des Apprenants et Formations

Un système complet de gestion des inscriptions, formations et paiements pour un centre de formation, développé avec **Spring Boot 3.5.5** et **PostgreSQL**.

## 🌟 Fonctionnalités

### Gestion des Apprenants
- ✅ Inscription des apprenants avec validation complète
- ✅ Recherche avancée (nom, prénom, email, âge, CIN)
- ✅ **Import/Export CSV et Excel** - NOUVEAU !
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
- **Import/Export**: Apache Commons CSV, Apache POI (Excel)
- **Build**: Maven

### Architecture en Couches
```
┌─────────────────────────────────────┐
│         Controllers REST            │  ← API REST avec Swagger
├─────────────────────────────────────┤
│           Services                  │  ← Logique métier + Import/Export
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

# Import/Export - Limites de fichiers
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

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

---

## 📥📤 API Import/Export Apprenants

### 🎯 Vue d'ensemble
L'API permet d'importer et exporter les données des apprenants en formats **CSV** et **Excel (.xlsx)** avec deux options d'export :
- **Export complet** : Toutes les données
- **Export paginé** : Seulement la page actuelle (pour intégration frontend)

### 📥 API d'Importation

#### Endpoint
```http
POST /api/apprenants/import
Content-Type: multipart/form-data
```

#### Formats supportés
- ✅ **CSV** : Support complet avec validation
- ✅ **Excel (.xlsx)** : Support avec limitation sur les dates

#### Structure de fichier requis
```csv
nom,prenom,email,telephone,adresse,dateNaissance,cin
```

#### Exemple de fichier CSV
```csv
nom,prenom,email,telephone,adresse,dateNaissance,cin
Dupont,Jean,jean.dupont@email.com,0123456789,123 Rue de la Paix,1990-05-15,CIN001
Martin,Marie,marie.martin@email.com,0234567890,456 Avenue du Soleil,1992-03-22,CIN002
```

#### Champs obligatoires
- `nom` : Nom de famille (max 50 caractères)
- `prenom` : Prénom (max 50 caractères)
- `email` : Email valide et unique (max 100 caractères)
- `cin` : Numéro CIN unique (max 20 caractères)

#### Champs optionnels
- `telephone` : Numéro de téléphone (max 20 caractères)
- `adresse` : Adresse postale (texte libre)
- `dateNaissance` : Date au format `yyyy-MM-dd`

### 📤 API d'Exportation

#### Endpoints disponibles

```http
# Export CSV complet
GET /api/apprenants/export/csv/all

# Export Excel complet
GET /api/apprenants/export/excel/all

# Export CSV paginé (pour frontend)
GET /api/apprenants/export/csv/page?page=0&size=20&sortBy=nom&sortDir=asc

# Export Excel paginé (pour frontend)
GET /api/apprenants/export/excel/page?page=0&size=20&sortBy=nom&sortDir=asc
```

#### Paramètres pour export paginé

| Paramètre | Type | Défaut | Description |
|-----------|------|--------|-------------|
| `page` | integer | 0 | Numéro de page (commence à 0) |
| `size` | integer | 20 | Nombre d'éléments par page |
| `sortBy` | string | "idApprenant" | Champ de tri |
| `sortDir` | string | "asc" | Direction: "asc" ou "desc" |

### 🔧 Intégration Frontend

#### Import avec React
```jsx
import React, { useState } from 'react';

const ImportComponent = () => {
  const [importing, setImporting] = useState(false);
  const [result, setResult] = useState(null);

  const handleImport = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    setImporting(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('/api/apprenants/import', {
        method: 'POST',
        body: formData
      });

      const result = await response.json();
      setResult(result);
      
      if (result.inserted > 0) {
        console.log(`${result.inserted} apprenants importés avec succès`);
      }
      
      if (result.errors && result.errors.length > 0) {
        console.warn('Erreurs détectées:', result.errors);
      }
    } catch (error) {
      console.error('Erreur lors de l\'import:', error);
    } finally {
      setImporting(false);
    }
  };

  return (
    <div className="import-section">
      <h3>Importer des apprenants</h3>
      
      <input
        type="file"
        accept=".csv,.xlsx"
        onChange={handleImport}
        disabled={importing}
      />
      
      {importing && <p>Import en cours...</p>}
      
      {result && (
        <div className="import-result">
          <h4>Résultat de l'import</h4>
          <p>Total traité: {result.total}</p>
          <p>Importé: {result.inserted}</p>
          <p>Ignoré: {result.skipped}</p>
          
          {result.errors && result.errors.length > 0 && (
            <div className="errors">
              <h5>Erreurs:</h5>
              {result.errors.map((error, index) => (
                <p key={index} className="error">
                  Ligne {error.rowNumber}: {error.message}
                </p>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ImportComponent;
```

#### Export avec React
```jsx
import React from 'react';

const ExportButtons = ({ currentPage = 0, pageSize = 20, totalElements = 0 }) => {
  const downloadFile = (blob, filename) => {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  };

  const handleExport = async (endpoint, filename) => {
    try {
      const response = await fetch(`/api/apprenants/export/${endpoint}`);
      if (!response.ok) throw new Error('Export failed');
      
      const blob = await response.blob();
      downloadFile(blob, filename);
    } catch (error) {
      console.error('Export error:', error);
      alert('Erreur lors de l\'exportation');
    }
  };

  return (
    <div className="export-section">
      <h3>Exporter les données</h3>
      
      <div className="export-buttons">
        <div className="export-group">
          <h4>Export complet ({totalElements} apprenants)</h4>
          <button 
            onClick={() => handleExport('csv/all', `apprenants_complet_${new Date().toISOString().slice(0,10)}.csv`)}
            className="btn btn-primary"
          >
            📄 CSV Complet
          </button>
          
          <button 
            onClick={() => handleExport('excel/all', `apprenants_complet_${new Date().toISOString().slice(0,10)}.xlsx`)}
            className="btn btn-success"
          >
            📊 Excel Complet
          </button>
        </div>

        <div className="export-group">
          <h4>Page courante ({Math.min(pageSize, totalElements - currentPage * pageSize)} apprenants)</h4>
          <button 
            onClick={() => handleExport(`csv/page?page=${currentPage}&size=${pageSize}`, `page_${currentPage + 1}.csv`)}
            className="btn btn-outline-primary"
          >
            📄 CSV Page {currentPage + 1}
          </button>
          
          <button 
            onClick={() => handleExport(`excel/page?page=${currentPage}&size=${pageSize}`, `page_${currentPage + 1}.xlsx`)}
            className="btn btn-outline-success"
          >
            📊 Excel Page {currentPage + 1}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ExportButtons;
```

### 📊 Réponses de l'API

#### Import réussi
```json
{
  "total": 4,
  "inserted": 4,
  "skipped": 0,
  "errors": []
}
```

#### Import avec erreurs
```json
{
  "total": 4,
  "inserted": 2,
  "skipped": 2,
  "errors": [
    {
      "rowNumber": 2,
      "message": "Champs obligatoires manquants (nom, prenom, email, cin)"
    },
    {
      "rowNumber": 4,
      "message": "Format dateNaissance invalide (yyyy-MM-dd)"
    }
  ]
}
```

#### Erreurs communes
- **Format de fichier non supporté** : Seuls CSV et XLSX sont acceptés
- **Champs obligatoires manquants** : nom, prénom, email, CIN requis
- **Email/CIN déjà existant** : Contrainte d'unicité
- **Format de date invalide** : Utiliser yyyy-MM-dd

### 🧪 Tests des API

#### Scripts de test fournis
```bash
# Test de l'API d'import
./test_import_api.sh

# Test de l'API d'export  
./test_export_api.sh
```

#### Exemples avec curl

```bash
# Import d'un fichier CSV
curl -X POST "http://localhost:8080/api/apprenants/import" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@apprenants.csv"

# Export CSV complet
curl -X GET "http://localhost:8080/api/apprenants/export/csv/all" \
  -H "Accept: text/csv" \
  -o "apprenants_export.csv"

# Export Excel paginé
curl -X GET "http://localhost:8080/api/apprenants/export/excel/page?page=0&size=10" \
  -H "Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" \
  -o "apprenants_page1.xlsx"
```

### ⚠️ Limitations actuelles
- **Excel dates** : Les cellules de date Excel ne sont pas parfaitement gérées
- **Taille de fichier** : Maximum 10MB
- **Formats supportés** : CSV et XLSX uniquement

### 🚀 Performance
- **Import** : ~100ms pour 10 lignes
- **Export complet** : ~200ms pour 100 apprenants  
- **Export paginé** : ~50ms par page

---

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
├── controller/          # Controllers REST + Import/Export
├── dto/                # Data Transfer Objects + Import Results
├── entity/             # Entités JPA
├── exception/          # Gestion des exceptions
├── repository/         # Repositories JPA
├── service/            # Services métier + Import/Export Services
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
- ✅ **Import/Export** sécurisé avec validation

### Tests
```bash
# Lancer tous les tests
./mvnw test

# Tests spécifiques
./mvnw test -Dtest=ApprenantControllerTest

# Tests des API Import/Export
./test_import_api.sh
./test_export_api.sh
```

## 🌐 Frontend

### CORS Configuration
Le backend est configuré pour accepter les requêtes depuis :
- React (http://localhost:3000)
- Angular (http://localhost:4200) 
- Vue.js (http://localhost:8081)

### Composants React Fournis
Dans le dossier `frontend_examples/` :
- `ExportButtons.jsx` - Composant complet d'export
- `ExportButtons.css` - Styles modernes
- `usage-example.jsx` - Exemple d'intégration
- `README.md` - Guide d'intégration détaillé

### Exemples d'Intégration Complète
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

// Import de fichier
const formData = new FormData();
formData.append('file', fileInput.files[0]);
const importResult = await fetch('/api/apprenants/import', {
  method: 'POST',
  body: formData
});

// Export avec téléchargement
const exportResponse = await fetch('/api/apprenants/export/csv/all');
const blob = await exportResponse.blob();
// Code de téléchargement...
```

## 📋 TODO / Améliorations Futures

### Fonctionnalités
- [ ] Système d'authentification/autorisation
- [ ] Notifications email/SMS
- [ ] Dashboard avec graphiques
- [ ] API de synchronisation mobile
- [ ] Système de notes et évaluations
- [x] Import/Export CSV/Excel ✅
- [ ] Import incrémental (mise à jour des données existantes)
- [ ] Export avec filtres personnalisés

### Technique  
- [ ] Tests d'intégration complets
- [ ] Cache Redis pour les performances
- [ ] Migration Flyway
- [ ] Monitoring avec Actuator
- [ ] Conteneurisation Docker
- [ ] CI/CD avec GitHub Actions
- [x] Parsing amélioré des dates Excel
- [ ] Support de formats additionnels (ODS, JSON)

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
- Utiliser les scripts de test fournis

### Documentation Supplémentaire
- `EXPORT-API-DOCUMENTATION.md` - Guide détaillé des exports
- `API_ENDPOINTS_SUMMARY.md` - Résumé de tous les endpoints
- `frontend_examples/README.md` - Guide d'intégration React

---

**Développé avec ❤️ et Spring Boot** 🚀

**Import/Export intégré avec Apache POI et Commons CSV** 📊
