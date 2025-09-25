# Tests de l'API d'import des apprenants

## ✅ Fonctionnalités activées

L'API d'import prend maintenant en charge :
- **Fichiers CSV** : Support complet avec validation
- **Fichiers Excel (.xlsx)** : Support activé avec limitation sur les dates

## Structure des fichiers de test

### apprenants_valides.csv
Fichier CSV contenant des données valides pour tester l'import réussi.
- 4 apprenants avec tous les champs requis
- Formats de dates corrects (yyyy-MM-dd)
- Emails et CINs uniques

### apprenants_erreurs.csv  
Fichier CSV contenant des données avec erreurs pour tester la gestion des erreurs :
- Ligne 2 : nom manquant
- Ligne 3 : prénom manquant  
- Ligne 4 : format de date invalide
- Ligne 5 : CIN manquant

### apprenants_excel_no_date.xlsx
Fichier Excel valide sans dates (fonctionne parfaitement)
- 3 apprenants importés avec succès
- Tous les champs sauf dateNaissance

### apprenants_excel.xlsx
Fichier Excel avec dates (problème connu)
- Échec de l'import à cause du format des dates Excel
- Les cellules de date Excel ne sont pas encore correctement converties

## Structure des données

Format CSV/Excel attendu :
```csv
nom,prenom,email,telephone,adresse,dateNaissance,cin
```

### Champs obligatoires
- nom : string (max 50 caractères)
- prenom : string (max 50 caractères)
- email : string valide (max 100 caractères, unique)
- cin : string (max 20 caractères, unique)

### Champs optionnels
- telephone : string (max 20 caractères)
- adresse : text
- dateNaissance : date au format yyyy-MM-dd

## Tests disponibles

Exécutez `./test_import_api.sh` pour lancer tous les tests automatiquement.

## Résultats

### ✅ CSV Import - Succès
```json
{
  "total": 4,
  "inserted": 4, 
  "skipped": 0,
  "errors": []
}
```

### ✅ Excel Import (sans dates) - Succès
```json
{
  "total": 3,
  "inserted": 3,
  "skipped": 0,
  "errors": []
}
```

### ❌ Excel Import (avec dates) - Limitation
```json
{
  "total": 3,
  "inserted": 0,
  "skipped": 3,
  "errors": [
    {
      "rowNumber": 2,
      "message": "Format dateNaissance invalide (yyyy-MM-dd)"
    }
  ]
}
```

## Limitations identifiées

1. **Gestion des dates Excel** : 
   - Les cellules de date Excel sont converties en nombre décimal
   - Nécessite une amélioration du parsing pour détecter et convertir les dates Excel
   - Contournement : utiliser des fichiers Excel avec dates en format texte ou pas de dates

## Prochaines améliorations suggérées

1. Améliorer le parsing des dates Excel avec `DateUtil.isCellDateFormatted()`
2. Supporter les formats de date Excel natifs
3. Ajouter plus de formats de date acceptés
