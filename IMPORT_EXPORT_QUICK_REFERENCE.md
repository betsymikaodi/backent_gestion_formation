# 📋 Import/Export API - Référence Rapide

## 🚀 Endpoints Import/Export

### Import
```http
POST /api/apprenants/import
Content-Type: multipart/form-data
Body: file=@fichier.csv|.xlsx
```

### Export
```http
GET /api/apprenants/export/csv/all          # CSV complet
GET /api/apprenants/export/excel/all        # Excel complet
GET /api/apprenants/export/csv/page         # CSV paginé
GET /api/apprenants/export/excel/page       # Excel paginé
```

## 📊 Structure de fichier

### CSV/Excel requis
```csv
nom,prenom,email,telephone,adresse,dateNaissance,cin
```

### Champs obligatoires
- `nom`, `prenom`, `email`, `cin`

### Champs optionnels  
- `telephone`, `adresse`, `dateNaissance`

## 🔧 Frontend React

### Import
```jsx
const formData = new FormData();
formData.append('file', file);
const response = await fetch('/api/apprenants/import', {
  method: 'POST',
  body: formData
});
const result = await response.json();
```

### Export
```jsx
const response = await fetch('/api/apprenants/export/csv/all');
const blob = await response.blob();
// Télécharger le fichier...
```

## 📦 Réponse Import
```json
{
  "total": 4,
  "inserted": 4,
  "skipped": 0,
  "errors": []
}
```

## 🧪 Tests
```bash
./test_import_api.sh   # Test import
./test_export_api.sh   # Test export
```

## 📚 Documentation complète
Voir `README.md` pour la documentation détaillée.
