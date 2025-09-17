# ⚡ Démarrage Rapide Thunder Client

## 🚀 **ÉTAPES POUR TESTER L'API EN 5 MINUTES**

### 1. **Démarrer l'Application Spring Boot**
```bash
cd /home/fandresena/Documents/projet/G_apprenant
./start-app.sh
```

**⏳ Attendre que l'application soit prête** (quand vous voyez "Started GApprenantApplication")

### 2. **Installer Thunder Client**
- Ouvrir VS Code
- `Ctrl+Shift+X` (Extensions)
- Chercher "Thunder Client"
- Installer l'extension

### 3. **Premier Test Simple**
Dans Thunder Client :
- **New Request**
- **URL** : `http://localhost:8080/api/apprenants`
- **Method** : `GET`
- **Send** ▶️

**✅ Si ça marche** → Vous verrez la liste des 5 apprenants !

---

## 🧪 **TEST IMMÉDIAT - LISTER TOUS LES ÉTUDIANTS**

### Configuration Thunder Client
```
Method: GET
URL: http://localhost:8080/api/apprenants
Headers: (aucun)
Body: (vide)
```

### Réponse Attendue
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
  },
  {
    "idApprenant": 2,
    "nom": "RABE", 
    "prenom": "Marie",
    "email": "marie.rabe@email.com",
    "telephone": "+261 33 11 222 33",
    "adresse": "Antsirabe",
    "dateNaissance": "1998-03-20",
    "cin": "234567890123",
    "inscriptions": []
  }
]
```

---

## 📊 **TESTS RAPIDES POUR TOUTES LES TABLES**

### ✅ **1. Apprenants**
```http
GET http://localhost:8080/api/apprenants
```

### ✅ **2. Formations**
```http
GET http://localhost:8080/api/formations
```

### ✅ **3. Inscriptions**
```http
GET http://localhost:8080/api/inscriptions
```

### ✅ **4. Paiements**
```http
GET http://localhost:8080/api/paiements
```

---

## ➕ **CRÉER UN NOUVEL APPRENANT**

### Configuration Thunder Client
```
Method: POST
URL: http://localhost:8080/api/apprenants
Headers: Content-Type: application/json
```

### Body (JSON)
```json
{
  "nom": "TEST",
  "prenom": "Utilisateur",
  "email": "test.utilisateur@email.com",
  "telephone": "+261 34 00 11 22",
  "adresse": "Test Address",
  "dateNaissance": "1990-01-01",
  "cin": "TEST123456789"
}
```

**✅ Résultat** → Nouvel apprenant créé avec ID automatique !

---

## 🔍 **RECHERCHER UN APPRENANT SPÉCIFIQUE**

### Par ID
```http
GET http://localhost:8080/api/apprenants/1
```

### Par nom
```http
GET http://localhost:8080/api/apprenants/search/nom?nom=RAKOTO
```

---

## 📝 **CRÉER UNE INSCRIPTION**

### Configuration Thunder Client
```
Method: POST
URL: http://localhost:8080/api/inscriptions
Headers: Content-Type: application/json
```

### Body (JSON)
```json
{
  "apprenantId": 1,
  "formationId": 2,
  "droitInscription": 25000.00
}
```

---

## 💰 **ENREGISTRER UN PAIEMENT**

### Configuration Thunder Client
```
Method: POST
URL: http://localhost:8080/api/paiements
Headers: Content-Type: application/json
```

### Body (JSON)
```json
{
  "inscriptionId": 1,
  "montant": 100000.00,
  "modePaiement": "Mobile Money",
  "module": "Module 1"
}
```

---

## 🚨 **DÉPANNAGE RAPIDE**

### ❌ **Connection Refused**
```bash
# L'application n'est pas démarrée
./start-app.sh
```

### ❌ **404 Not Found**
```bash
# Vérifier l'URL - doit commencer par http://localhost:8080/api
```

### ❌ **500 Internal Error**
```bash
# Email ou CIN déjà existant - changer les valeurs
```

---

## 📁 **IMPORTER LA COLLECTION COMPLÈTE**

1. Dans Thunder Client → **Import**
2. Sélectionner le fichier `thunder-collection.json`
3. **Import** ✅
4. Vous aurez toutes les requêtes prêtes !

---

## ✨ **BONUS : WORKFLOW COMPLET**

### Scénario : Inscription d'un nouvel apprenant à une formation

1. **GET** `/apprenants` → Voir les apprenants existants
2. **GET** `/formations` → Choisir une formation (noter l'ID)
3. **POST** `/apprenants` → Créer un nouvel apprenant (noter l'ID)
4. **POST** `/inscriptions` → Inscrire l'apprenant à la formation
5. **POST** `/paiements` → Enregistrer un premier paiement
6. **GET** `/inscriptions` → Vérifier l'inscription créée

---

## 🎯 **RÉSUMÉ : 3 COMMANDES ESSENTIELLES**

### 🥇 **Lister tous les apprenants**
```http
GET http://localhost:8080/api/apprenants
```

### 🥈 **Créer un apprenant**
```http
POST http://localhost:8080/api/apprenants
Content-Type: application/json

{
  "nom": "NOUVEAU",
  "prenom": "Apprenant",
  "email": "nouveau@email.com",
  "cin": "123456789999"
}
```

### 🥉 **Lister toutes les formations**
```http
GET http://localhost:8080/api/formations
```

---

## 🎉 **VOUS ÊTES PRÊT !**

Avec ces 3 requêtes de base, vous pouvez :
- ✅ **Voir** tous les étudiants
- ✅ **Créer** de nouveaux étudiants  
- ✅ **Explorer** les formations disponibles

**Pour la suite** → Consultez `THUNDER-CLIENT-GUIDE.md` pour tous les détails CRUD complets !

---

*💡 Conseil : Sauvegardez vos requêtes dans une collection Thunder Client pour les réutiliser facilement !*