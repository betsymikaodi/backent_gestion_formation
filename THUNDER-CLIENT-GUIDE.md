# ⚡ Guide Thunder Client - API CRUD Complet

## 🚀 Configuration Initiale

### 1. Installer Thunder Client dans VS Code
- Ouvrir VS Code
- Aller dans Extensions (Ctrl+Shift+X)
- Chercher "Thunder Client" 
- Installer l'extension de Ranga Vadhineni

### 2. Démarrer l'application
```bash
cd /home/fandresena/Documents/projet/G_apprenant
./start-app.sh
```

### 3. URL de base
```
http://localhost:8080/api
```

---

## 👥 **GESTION DES APPRENANTS** (`/apprenants`)

### ✅ **GET** - Lister tous les apprenants
- **URL** : `http://localhost:8080/api/apprenants`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **GET** - Obtenir un apprenant par ID
- **URL** : `http://localhost:8080/api/apprenants/1`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **POST** - Créer un nouvel apprenant
- **URL** : `http://localhost:8080/api/apprenants`
- **Method** : `POST`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "nom": "NOUVEAU",
    "prenom": "Etudiant",
    "email": "nouveau.etudiant@email.com",
    "telephone": "+261 34 99 88 77",
    "adresse": "Antananarivo, Madagascar",
    "dateNaissance": "1995-06-15",
    "cin": "999888777666"
  }
  ```

### ✅ **PUT** - Modifier un apprenant existant
- **URL** : `http://localhost:8080/api/apprenants/1`
- **Method** : `PUT`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "nom": "RAKOTO",
    "prenom": "Jean Pierre",
    "email": "jeanpierre.rakoto@email.com",
    "telephone": "+261 34 12 345 67",
    "adresse": "Lot ABC Bis Antananarivo",
    "dateNaissance": "1995-05-15",
    "cin": "123456789012"
  }
  ```

### ✅ **DELETE** - Supprimer un apprenant
- **URL** : `http://localhost:8080/api/apprenants/6`
- **Method** : `DELETE`
- **Headers** : Aucun
- **Body** : Aucun

### 🔍 **GET** - Recherches avancées
```
# Par nom
http://localhost:8080/api/apprenants/search/nom?nom=RAKOTO

# Avec pagination
http://localhost:8080/api/apprenants/paginated?page=0&size=3&sortBy=nom&sortDir=asc
```

---

## 📚 **GESTION DES FORMATIONS** (`/formations`)

### ✅ **GET** - Lister toutes les formations
- **URL** : `http://localhost:8080/api/formations`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **GET** - Obtenir une formation par ID
- **URL** : `http://localhost:8080/api/formations/1`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **POST** - Créer une nouvelle formation
- **URL** : `http://localhost:8080/api/formations`
- **Method** : `POST`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "nom": "Intelligence Artificielle",
    "description": "Formation complète sur l'IA et Machine Learning",
    "duree": 150,
    "frais": 750000.00
  }
  ```

### ✅ **PUT** - Modifier une formation existante
- **URL** : `http://localhost:8080/api/formations/1`
- **Method** : `PUT`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "nom": "Développement Web Full Stack Avancé",
    "description": "Formation complète en développement web avec React, Spring Boot et DevOps",
    "duree": 140,
    "frais": 550000.00
  }
  ```

### ✅ **DELETE** - Supprimer une formation
- **URL** : `http://localhost:8080/api/formations/6`
- **Method** : `DELETE`
- **Headers** : Aucun
- **Body** : Aucun

### 🔍 **GET** - Recherches spécialisées
```
# Par fourchette de prix
http://localhost:8080/api/formations/search/prix?fraisMin=200000&fraisMax=400000

# Par durée
http://localhost:8080/api/formations/search/duree?dureeMin=60&dureeMax=120

# Formations populaires
http://localhost:8080/api/formations/populaires?limit=3

# Prix moyen des formations
http://localhost:8080/api/formations/stats/moyenne-prix
```

---

## 📝 **GESTION DES INSCRIPTIONS** (`/inscriptions`)

### ✅ **GET** - Lister toutes les inscriptions
- **URL** : `http://localhost:8080/api/inscriptions`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **GET** - Obtenir une inscription par ID
- **URL** : `http://localhost:8080/api/inscriptions/1`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **POST** - Créer une nouvelle inscription
- **URL** : `http://localhost:8080/api/inscriptions`
- **Method** : `POST`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "apprenantId": 2,
    "formationId": 3,
    "droitInscription": 20000.00
  }
  ```

### ✅ **PUT** - Modifier une inscription existante
- **URL** : `http://localhost:8080/api/inscriptions/1`
- **Method** : `PUT`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "apprenantId": 1,
    "formationId": 1,
    "statut": "Confirmé",
    "droitInscription": 30000.00
  }
  ```

### ✅ **DELETE** - Supprimer une inscription
- **URL** : `http://localhost:8080/api/inscriptions/7`
- **Method** : `DELETE`
- **Headers** : Aucun
- **Body** : Aucun

---

## 💰 **GESTION DES PAIEMENTS** (`/paiements`)

### ✅ **GET** - Lister tous les paiements
- **URL** : `http://localhost:8080/api/paiements`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **GET** - Obtenir un paiement par ID
- **URL** : `http://localhost:8080/api/paiements/1`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun

### ✅ **GET** - Obtenir les paiements d'une inscription
- **URL** : `http://localhost:8080/api/paiements/inscription/1`
- **Method** : `GET`
- **Headers** : Aucun
- **Body** : Aucun


### ✅ **POST** - Enregistrer un nouveau paiement
- **URL** : `http://localhost:8080/api/paiements`
- **Method** : `POST`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON)** :
  ```json
  {
    "inscriptionId": 1,
    "montant": 125000.00,
    "modePaiement": "Mobile Money",
    "module": "Module 4"
  }
  ```

### ✅ **PUT** - Modifier un paiement existant
- **URL** : `http://localhost:8080/api/paiements/1`
- **Method** : `PUT`
- **Headers** : 
  ```
  Content-Type: application/json
  ```
- **Body (JSON - champs optionnels)** :
  ```json
  {
    "montant": 130000.00,
    "modePaiement": "Virement",
    "module": "Module 1"
  }
  ```

### ✅ **DELETE** - Supprimer un paiement
- **URL** : `http://localhost:8080/api/paiements/12`
- **Method** : `DELETE`
- **Headers** : Aucun
- **Body** : Aucun

---

## 🔧 **ÉTAPES PRATIQUES DANS THUNDER CLIENT**

### 1. **Créer une Collection**
1. Ouvrir Thunder Client dans VS Code
2. Cliquer sur "New Collection"
3. Nommer : "G_apprenant API"

### 2. **Créer les Requêtes**
1. Cliquer sur "New Request" dans votre collection
2. Nommer la requête (ex: "GET - Tous les apprenants")
3. Configurer l'URL, méthode, headers et body selon les exemples ci-dessus

### 3. **Organisation Suggérée**
```
📁 G_apprenant API/
├── 📁 1. Apprenants/
│   ├── GET - Tous les apprenants
│   ├── GET - Apprenant par ID
│   ├── POST - Créer apprenant
│   ├── PUT - Modifier apprenant
│   └── DELETE - Supprimer apprenant
├── 📁 2. Formations/
│   ├── GET - Toutes les formations
│   ├── GET - Formation par ID
│   ├── POST - Créer formation
│   ├── PUT - Modifier formation
│   └── DELETE - Supprimer formation
├── 📁 3. Inscriptions/
│   ├── GET - Toutes les inscriptions
│   ├── POST - Créer inscription
│   └── PUT - Modifier inscription
└── 📁 4. Paiements/
    ├── GET - Tous les paiements
    ├── POST - Créer paiement
    └── PUT - Modifier paiement
```

---

## 🧪 **TESTS RECOMMANDÉS**

### Test 1 : CRUD Complet Apprenant
1. **GET** `/apprenants` → Voir la liste actuelle
2. **POST** `/apprenants` → Créer un nouvel apprenant
3. **GET** `/apprenants/{id}` → Vérifier la création
4. **PUT** `/apprenants/{id}` → Modifier l'apprenant
5. **DELETE** `/apprenants/{id}` → Supprimer l'apprenant

### Test 2 : Workflow Complet Inscription
1. **GET** `/apprenants` → Choisir un apprenant (noter l'ID)
2. **GET** `/formations` → Choisir une formation (noter l'ID)
3. **POST** `/inscriptions` → Créer l'inscription
4. **POST** `/paiements` → Enregistrer un paiement

### Test 3 : Recherches Avancées
1. **GET** `/apprenants/search/nom?nom=RAKOTO`
2. **GET** `/formations/search/prix?fraisMin=200000&fraisMax=500000`
3. **GET** `/formations/populaires?limit=3`

---

## ❌ **GESTION DES ERREURS**

### Erreurs Courantes et Solutions

#### 🔴 **400 Bad Request**
- **Cause** : Données JSON invalides ou champs manquants
- **Solution** : Vérifier le format JSON et les champs obligatoires

#### 🔴 **404 Not Found**
- **Cause** : ID inexistant ou URL incorrecte  
- **Solution** : Vérifier l'ID et l'URL

#### 🔴 **500 Internal Server Error**
- **Cause** : Violation de contrainte (email/CIN déjà existant)
- **Solution** : Utiliser des valeurs uniques

#### 🔴 **Connection Refused**
- **Cause** : Application Spring Boot non démarrée
- **Solution** : Lancer `./start-app.sh`

---

## 📊 **EXEMPLES DE RÉPONSES**

### Réponse GET /apprenants
```json
[
  {
    "idApprenant": 1,
    "nom": "RAKOTO",
    "prenom": "Jean",
    "email": "jean.rakoto@email.com",
    "telephone": "+261 34 12 345 67",
    "adresse": "Lot ABC Antananarivo",
    "dateNaissance": "1995-05-15",
    "cin": "123456789012",
    "inscriptions": []
  }
]
```

### Réponse POST /formations (201 Created)
```json
{
  "idFormation": 6,
  "nom": "Intelligence Artificielle",
  "description": "Formation complète sur l'IA et Machine Learning",
  "duree": 150,
  "frais": 750000.00,
  "inscriptions": []
}
```

---

## 🎯 **CONSEILS POUR THUNDER CLIENT**

### ✅ **Bonnes Pratiques**
1. **Sauvegarder** vos requêtes dans des collections
2. **Utiliser des variables** pour l'URL de base
3. **Tester les erreurs** avec des données invalides
4. **Vérifier les réponses** avant/après chaque opération

### 🔧 **Variables d'Environnement**
Créer une variable `baseUrl` = `http://localhost:8080/api`
Puis utiliser `{{baseUrl}}/apprenants`

### 📝 **Documentation**
Thunder Client génère automatiquement la documentation de vos tests !

---

## 🚀 **DÉMARRAGE RAPIDE**

1. **Lancer l'app** : `./start-app.sh`
2. **Ouvrir Thunder Client** dans VS Code
3. **Tester** : `GET http://localhost:8080/api/apprenants`
4. **Si ça marche** → Vous avez tous les apprenants ! ✅
5. **Créer** votre collection avec tous les endpoints ci-dessus

**🎉 Vous pouvez maintenant faire tous les CRUD sur toutes les tables avec Thunder Client !**