# API Apprenants - Exemples d'utilisation

## 🎯 Endpoint Principal
**GET /apprenants** - Recherche avec pagination et tri professionnel

## 📊 Exemples de Requêtes

### 1. Pagination de base
```
GET /apprenants?page=0&size=20
GET /apprenants?page=1&size=50
GET /apprenants?page=0&size=100
```

### 2. Tri professionnel
```
# Tri par nom croissant
GET /apprenants?sortBy=nom&sortDirection=asc

# Tri par date de création décroissant (défaut)
GET /apprenants?sortBy=dateNow&sortDirection=desc

# Tri par email croissant
GET /apprenants?sortBy=email&sortDirection=asc
```

### 3. Recherche globale
```
# Recherche "john" dans nom, prénom, email
GET /apprenants?search=john

# Recherche avec pagination
GET /apprenants?search=martin&page=0&size=10
```

### 4. Filtres spécifiques
```
# Filtrer par nom
GET /apprenants?nom=dupont

# Filtrer par email
GET /apprenants?email=gmail.com

# Filtrer par téléphone
GET /apprenants?telephone=034
```

### 5. Filtres avancés
```
# Par période de naissance
GET /apprenants?dateNaissanceDebut=1990-01-01&dateNaissanceFin=2000-12-31

# Par statut d'inscription
GET /apprenants?statutInscription=active

# Par formation
GET /apprenants?formationId=1
GET /apprenants?nomFormation=java
```

### 6. Combinaisons complexes
```
# Recherche + tri + pagination
GET /apprenants?search=martin&sortBy=nom&sortDirection=asc&page=0&size=20

# Filtres multiples
GET /apprenants?nom=martin&email=gmail&sortBy=prenom&size=50

# Recherche par formation avec tri
GET /apprenants?nomFormation=spring&sortBy=dateNow&sortDirection=desc&size=25
```

## 📋 Structure de la Réponse

```json
{
  "data": [
    {
      "idApprenant": 1,
      "nom": "Martin",
      "prenom": "Jean",
      "email": "jean.martin@gmail.com",
      "telephone": "0341234567",
      "adresse": "123 Rue de la Paix",
      "dateNaissance": "1995-05-15",
      "cin": "1234567890123",
      "dateNow": "2024-01-15T10:30:00",
      "inscriptions": [...]
    }
  ],
  "pagination": {
    "current_page": 0,
    "page_size": 20,
    "total_elements": 156,
    "total_pages": 8,
    "has_next": true,
    "has_previous": false,
    "first_page": true,
    "last_page": false
  }
}
```

## 🔧 Endpoints Utilitaires

### Tailles de page disponibles
```
GET /apprenants/sizes
```
Retourne: `[10, 20, 50, 100]`

### Options de tri disponibles  
```
GET /apprenants/sort-options
```
Retourne: `["nom", "prenom", "email", "telephone", "dateNaissance", "dateNow"]`

### Nombre total d'apprenants
```
GET /apprenants/count
```
Retourne: `156`

## 🚀 Interface Front-end

### Navigation
```javascript
// Page suivante
const nextPage = currentPage + 1;
fetch(`/apprenants?page=${nextPage}&size=${pageSize}`);

// Page précédente  
const prevPage = currentPage - 1;
fetch(`/apprenants?page=${prevPage}&size=${pageSize}`);

// Aller à la page X
fetch(`/apprenants?page=${targetPage}&size=${pageSize}`);
```

### Tri dynamique
```javascript
// Changer le tri
const changeSorting = (field, direction) => {
  fetch(`/apprenants?sortBy=${field}&sortDirection=${direction}&page=0&size=${pageSize}`);
};
```

### Recherche en temps réel
```javascript
// Recherche avec debounce
const searchApprenants = (searchTerm) => {
  fetch(`/apprenants?search=${encodeURIComponent(searchTerm)}&page=0&size=${pageSize}`);
};
```

## ✅ Avantages de cette Implementation

1. **Pagination flexible** : 10, 20, 50, 100 éléments par page
2. **Tri multi-critères** : Tous les champs principaux  
3. **Recherche globale** : Dans nom, prénom, email
4. **Filtres avancés** : Par tous les champs
5. **Métadonnées complètes** : Navigation suivant/précédent
6. **Performance optimisée** : Requêtes JPA Specification
7. **Documentation Swagger** : Auto-générée
8. **Response standardisée** : Format cohérent
